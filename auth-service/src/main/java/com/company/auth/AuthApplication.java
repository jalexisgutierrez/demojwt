package com.company.auth;

import com.company.auth.application.service.UserAdminUseCase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Bootstrap: crea ADMIN si no existe
 */
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    CommandLineRunner seedAdmin(UserAdminUseCase admin) {
        return args -> admin.ensureAdminExists("admin@mail.com","admin");
    }
}
