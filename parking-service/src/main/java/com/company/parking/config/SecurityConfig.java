package com.company.parking.config;

import com.company.parking.adapters.in.security.JwtAuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Instant;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwt;


    public SecurityConfig(JwtAuthFilter jwt) {
        this.jwt = jwt;
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/actuator", "/actuator/health", "/actuator/health/**").permitAll()
                        .requestMatchers("/lots/**").hasAnyRole("ADMIN","SOCIO")
                        .requestMatchers("/entries/**").hasAnyRole("ADMIN","SOCIO")
                        .requestMatchers("/indicators/**").hasAnyRole("ADMIN","SOCIO")
                        .anyRequest().authenticated()
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint(rest401())
                        .accessDeniedHandler(rest403())
                )
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }

    @Bean
    AuthenticationEntryPoint rest401() {
        return (req, res, ex) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            new ObjectMapper().writeValue(res.getOutputStream(), Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 401, "error", "Unauthorized",
                    "message", "Token inválido o ausente",
                    "path", req.getRequestURI()
            ));
        };
    }

    @Bean
    AccessDeniedHandler rest403() {
        return (req, res, ex) -> {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setContentType("application/json");
            new ObjectMapper().writeValue(res.getOutputStream(), Map.of(
                    "timestamp", Instant.now().toString(),
                    "status", 403, "error", "Forbidden",
                    "message", "No tienes permisos para esta operación",
                    "path", req.getRequestURI()
            ));
        };
    }
}
