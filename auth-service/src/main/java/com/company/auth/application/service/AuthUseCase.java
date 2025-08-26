package com.company.auth.application.service;

import com.company.auth.domain.model.User;
import com.company.auth.domain.port.*;
import com.company.auth.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Login: valida credenciales y emite JWT. */
@Service @RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepositoryPort users;
    private final PasswordHasherPort hasher;
    private final TokenServicePort tokens;

    public TokenServicePort.Token login(String email, String password){
        User u = users.findByEmail(email).orElseThrow(() -> new BadRequestException("Credenciales inválidas"));
        if (!hasher.matches(password, u.getPasswordHash()))
            throw new BadRequestException("Credenciales inválidas");
        return tokens.create(u);
    }
}
