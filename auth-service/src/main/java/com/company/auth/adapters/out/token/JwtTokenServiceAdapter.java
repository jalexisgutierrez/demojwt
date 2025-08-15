package com.company.auth.adapters.out.token;

import com.company.auth.domain.model.User;
import com.company.auth.domain.port.TimeProviderPort;
import com.company.auth.domain.port.TokenServicePort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Servicio de emisión/lectura de JWT (HS256)
 * Claims sub = email, uid = userId (si existe), role = ADMIN | SOCIO
 */
@Component
public class JwtTokenServiceAdapter implements TokenServicePort {

    private final SecretKey key;

    private final long expirationMs;

    private final TimeProviderPort time;

    public JwtTokenServiceAdapter(@Value("${app.jwt.secret}") String secret,
                                  @Value("${app.jwt.expiration}") long expirationMs,
                                  TimeProviderPort time) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.time = time;
    }

    @Override
    public Token create(User user) {
        long now = time.nowMillis();
        long exp = now + expirationMs;

        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("uid", user.getId()==null?null:user.getId().toString())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new Token(token, now, exp);
    }

    @Override
    public String roleFromToken(String token) {
        var claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        Object r = claims.get("role");
        return r==null ? null : r.toString();
    }
}
