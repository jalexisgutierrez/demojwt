package com.company.auth.config;

import com.company.auth.adapters.in.security.JwtAuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;
import java.util.Map;

@Configuration
@EnableMethodSecurity // <- si planeas usar @PreAuthorize en controladores/servicios
public class SecurityConfig {

    private final JwtAuthFilter jwt;

    public SecurityConfig(JwtAuthFilter jwt) {
        this.jwt = jwt;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/actuator/health").permitAll()
                        .requestMatchers("/auth/register-socio").hasRole("ADMIN") // <- solo ADMIN
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthEntryPoint())  // 401 con JSON
                        .accessDeniedHandler(restAccessDeniedHandler())  // 403 con JSON
                )
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    // ====== Handlers para respuestas JSON en 401/403 ======

    @Bean
    AuthenticationEntryPoint restAuthEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            var body = Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 401,
                    "error", "Unauthorized",
                    "message", "Token inválido u ausente",
                    "path", request.getRequestURI()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), body);
        };
    }

    @Bean
    AccessDeniedHandler restAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.setContentType("application/json");
            var body = Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 403,
                    "error", "Forbidden",
                    "message", "No tienes permisos para esta operación",
                    "path", request.getRequestURI()
            );
            new ObjectMapper().writeValue(response.getOutputStream(), body);
        };
    }
}
