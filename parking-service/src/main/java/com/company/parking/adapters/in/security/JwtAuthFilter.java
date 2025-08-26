package com.company.parking.adapters.in.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtParser parser;

    public JwtAuthFilter(@Value("${app.jwt.secret}") String secret) {
        if (!StringUtils.hasText(secret)) {
            throw new IllegalStateException("APP_JWT_SECRET no está definido");
        }
        SecretKey key;
        try {
            // **Siempre UTF-8 crudo**, igual que en auth-service
            key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        } catch (WeakKeyException e) {
            throw new IllegalStateException("APP_JWT_SECRET es muy corto (HS256 requiere >= 32 bytes).", e);
        }
        this.parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {


        String header = req.getHeader("Authorization");

        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }
        String token = header.substring(7);
        try {
            Jws<Claims> jws = parser.parseClaimsJws(token);
            Claims c = jws.getBody();

            String email = c.getSubject();
            String role  = String.valueOf(c.get("role"));
            String uid   = String.valueOf(c.get("uid"));

            if (!StringUtils.hasText(email) || !StringUtils.hasText(role)) {
                unauthorized(res, req, "Token inválido: claims incompletos");
                return;
            }

            var authority = new SimpleGrantedAuthority("ROLE_" + role);
            var principal = new UserPrincipal(uid, email);
            var authToken = new UsernamePasswordAuthenticationToken(principal, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            chain.doFilter(req, res);

        } catch (ExpiredJwtException e) {
            unauthorized(res, req, "Token expirado");
        } catch (JwtException e) {
            unauthorized(res, req, "Token inválido o ausente");
        }
    }

    private void unauthorized(HttpServletResponse res, HttpServletRequest req, String msg) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write(
                "{\"path\":\"" + req.getRequestURI() + "\"," +
                        "\"status\":401," +
                        "\"message\":\"" + msg + "\"," +
                        "\"error\":\"Unauthorized\"}"
        );
    }

    public record UserPrincipal(String uid, String email) {}
}
