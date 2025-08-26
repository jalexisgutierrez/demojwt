package com.company.auth.domain.port;

public interface PasswordHasherPort {

    String hash(String raw);

    boolean matches(String raw, String hash);
}
