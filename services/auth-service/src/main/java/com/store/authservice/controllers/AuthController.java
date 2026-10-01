package com.store.authservice.controllers;

import com.store.authservice.dto.UserDTO;
import com.store.authservice.dto.UserLoginDTO;
import com.store.authservice.dto.UserRegistrationDTO;
import com.store.authservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        authService.register(userRegistrationDTO);
        return ResponseEntity.ok().body(userRegistrationDTO.getEmail());
    }

    @PostMapping("/api/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        return  ResponseEntity.ok().body(authService.login(userLoginDTO));
    }
}
