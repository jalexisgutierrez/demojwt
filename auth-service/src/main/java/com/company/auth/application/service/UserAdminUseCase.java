package com.company.auth.application.service;

import com.company.auth.domain.model.User;
import com.company.auth.domain.model.UserRole;
import com.company.auth.domain.port.PasswordHasherPort;
import com.company.auth.domain.port.UserRepositoryPort;
import com.company.auth.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserAdminUseCase {

    private final UserRepositoryPort users;
    private final PasswordHasherPort hasher;

    /** Crea ADMIN si no existe (bootstrap). */
    public void ensureAdminExists(String email, String rawPass){
        if (!users.existsByEmail(email)) {
            var admin = User.builder()
                    .email(email)
                    .passwordHash(hasher.hash(rawPass))
                    .role(UserRole.ADMIN)
                    .createdAt(Instant.now())
                    .build();
            users.save(admin);
        }
    }

    /** Crea SOCIO (debe ser llamado por ADMIN desde el controlador). */
    public User createSocio(String email, String rawPass){
        if (users.existsByEmail(email)) throw new BadRequestException("Email ya existe");
        var socio = User.builder()
                .email(email)
                .passwordHash(hasher.hash(rawPass))
                .role(UserRole.SOCIO)
                .createdAt(Instant.now())
                .build();
        return users.save(socio);
    }
}
