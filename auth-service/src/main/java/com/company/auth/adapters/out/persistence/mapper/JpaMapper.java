package com.company.auth.adapters.out.persistence.mapper;

import com.company.auth.adapters.out.persistence.entity.UserEntity;
import com.company.auth.domain.model.User;
import com.company.auth.domain.model.UserRole;

public class JpaMapper {

    public static User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .passwordHash(userEntity.getPasswordHash())
                .role(UserRole.valueOf(userEntity.getRole()))
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
