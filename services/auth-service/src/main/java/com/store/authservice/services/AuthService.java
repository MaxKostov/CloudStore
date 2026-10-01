package com.store.authservice.services;

import com.store.authservice.dto.AuthorizedResponse;
import com.store.authservice.dto.LoginRequest;
import com.store.authservice.dto.RegistrationRequest;

public interface AuthService {
    AuthorizedResponse register(RegistrationRequest registrationRequest);

    AuthorizedResponse login(LoginRequest loginRequest);

    void logout(String token);
}
