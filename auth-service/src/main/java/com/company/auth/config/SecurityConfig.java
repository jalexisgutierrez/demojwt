package com.company.auth.config;

import com.company.auth.adapters.in.security.JwtAuthFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwt;
}
