package com.company.auth.adapters.in.web;

import com.company.auth.adapters.in.web.dto.LoginRequest;
import com.company.auth.adapters.in.web.dto.LoginResponse;
import com.company.auth.adapters.in.web.dto.RegisterSocioRequest;
import com.company.auth.application.service.AuthUseCase;
import com.company.auth.application.service.UserAdminUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints públicos/privados del auth-service
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase auth;
    private final UserAdminUseCase admin;

    /** Público: valida credenciales y retorna JWT (6h por defecto) */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) throws BadRequestException {
        var t = auth.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(t.value(), t.issuedAt(), t.expiresAt()));
    }

    /** Solo ADMIN: crea un SOCIO. Protegido por JwtAuthFilter + @PreAuthorize */
    @PostMapping("/register-socio")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerSocio(@RequestBody @Valid RegisterSocioRequest req) throws BadRequestException {
        admin.createSocio(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(java.util.Map.of("message","SOCIO creado"));
    }
}
