package com.company.parking.adapters.in.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
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
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtParser parser;

    public JwtAuthFilter(@Value("${app.jwt.secret}") String secret) {
        // Acepta secreto en Base64 o como texto plano UTF-8 (misma lógica en auth-service)
        byte[] keyBytes;
        if (secret != null && secret.matches("^[A-Za-z0-9+/=]+$")) {
            try {
                keyBytes = Decoders.BASE64.decode(secret);
            } catch (Exception ignore) {
                keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            }
        } else {
            keyBytes = Optional.ofNullable(secret).orElse("").getBytes(StandardCharsets.UTF_8);
        }

        try {
            SecretKey key = Keys.hmacShaKeyFor(keyBytes); // lanza WeakKeyException si < 256 bits
            this.parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(60) // tolerancia de reloj
                    .build();
        } catch (WeakKeyException e) {
            // Fallo temprano y explícito si la clave es débil
            throw new IllegalStateException("APP_JWT_SECRET es muy corto (HS256 requiere >= 32 bytes).", e);
        }
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

            String email = c.getSubject();          // "sub"
            String role  = String.valueOf(c.get("role")); // "ADMIN" | "SOCIO"
            String uid   = String.valueOf(c.get("uid"));

            if (!StringUtils.hasText(email) || !StringUtils.hasText(role)) {
                unauthorized(res, req, "Token inválido: claims incompletos");
                return;
            }

            // Spring Security espera ROLE_
            var authority = new SimpleGrantedAuthority("ROLE_" + role);
            var principal = new UserPrincipal(uid, email);
            var authToken = new UsernamePasswordAuthenticationToken(principal, null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            chain.doFilter(req, res);

        } catch (ExpiredJwtException e) {
            unauthorized(res, req, "Token expirado");
        } catch (JwtException e) { // firma inválida, mal formado, algoritmo distinto, etc.
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
