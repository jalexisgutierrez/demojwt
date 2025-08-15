package com.company.auth.adapters.out.crypto;

import com.company.auth.domain.port.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/** Hashing de contraseñas con BCrypt*/
@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

    @Override
    public String hash(String raw) {
        return enc.encode(raw);
    }

    @Override
    public boolean matches(String raw, String hash) {
        return enc.matches(raw, hash);
    }
}
