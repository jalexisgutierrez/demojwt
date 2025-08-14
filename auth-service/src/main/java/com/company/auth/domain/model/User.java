package com.company.auth.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

/**
 * Entidad dominio
 */
@Value
@Builder
public class User {
    UUID id;
    String email;
    String passwordHash;
    UserRole role;
    Instant createdAt;
}
