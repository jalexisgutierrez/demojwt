package com.example.demo.auth;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        var res = service.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(res);
    }
}
