package com.company.auth.adapters.in.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey key;
    public JwtAuthFilter(@Value("${app.jwt.secret}") String secret){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req,
                                    @NonNull HttpServletResponse res,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        String token = (StringUtils.hasText(header) && header.startsWith("Bearer ")) ? header.substring(7) : null;

        if (token != null && SecurityContextHolder.getContext().getAuthentication()==null) {
            Claims c = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String email = c.getSubject();
            String role  = String.valueOf(c.get("role"));
            var auth = new UsernamePasswordAuthenticationToken(email, null,
                    Set.of(new SimpleGrantedAuthority("ROLE_" + role)).stream().toList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(req,res);
    }
}
