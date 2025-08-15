package com.company.auth.adapters.out.persistence;

import com.company.auth.adapters.out.persistence.mapper.JpaMapper;
import com.company.auth.adapters.out.persistence.repo.SpringDataUserRepository;
import com.company.auth.domain.model.User;
import com.company.auth.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaUserAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repo;

    @Override
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email).map(JpaMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return JpaMapper.toDomain(repo.save(JpaMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repo.findById(id).map(JpaMapper::toDomain);
    }
}
