package com.example.demo.auth;

import com.example.demo.dtos.LoginResponse;
import com.example.demo.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserDetailsService uds;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authManager, UserDetailsService uds, JwtService jwtService) {
        this.authManager = authManager;
        this.uds = uds;
        this.jwtService = jwtService;
    }

    public LoginResponse login(String username, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        var user = uds.loadUserByUsername(username);
        String token = jwtService.generateToken(user);
        long now = System.currentTimeMillis();
        long exp = now + jwtService.getExpirationMs();
        return new LoginResponse(token, now, exp);
    }
}
