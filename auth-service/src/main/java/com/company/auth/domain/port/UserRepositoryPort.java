package com.company.auth.domain.port;

import com.company.auth.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User save(User user);

    Optional<User> findById(UUID id);
}
