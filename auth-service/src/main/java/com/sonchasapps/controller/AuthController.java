package com.sonchasapps.controller;

import com.sonchasapps.dto.AuthResponce;
import com.sonchasapps.dto.LogInRequest;
import com.sonchasapps.dto.RegisterRequest;
import com.sonchasapps.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponce> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponce> login(@RequestBody LogInRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}
