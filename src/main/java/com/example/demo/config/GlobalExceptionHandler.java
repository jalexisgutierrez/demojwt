package com.example.demo.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpired(ExpiredJwtException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "token_expired");
        body.put("message", "El token ha expirado. Vuelve a autenticarse.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
