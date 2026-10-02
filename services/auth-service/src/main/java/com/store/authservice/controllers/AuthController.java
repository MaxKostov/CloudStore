package com.store.authservice.controllers;

import com.store.authservice.dto.responses.AuthorizedResponse;
import com.store.authservice.dto.requests.LoginRequest;
import com.store.authservice.dto.requests.RegistrationRequest;
import com.store.authservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<AuthorizedResponse> registerUser(
            @Valid @RequestBody RegistrationRequest registrationRequest
    ) {
        AuthorizedResponse response = authService.register(registrationRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthorizedResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthorizedResponse response = authService.login(loginRequest);
        return  ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }
}
