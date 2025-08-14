package com.company.auth.domain.port;

import com.company.auth.domain.model.User;

public interface TokenServicePort {

    /** Devuelve token + epoch millis emit/exp en un record simple*/
    record Token(String value, long issuedAt, long expiresAt) {}

    Token create(User user);

    String roleFromToken(String token);
}
