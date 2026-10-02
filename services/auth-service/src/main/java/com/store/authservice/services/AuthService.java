package com.store.authservice.services;

import com.store.authservice.dto.responses.AuthorizedResponse;
import com.store.authservice.dto.requests.LoginRequest;
import com.store.authservice.dto.requests.RegistrationRequest;

public interface AuthService {
    AuthorizedResponse register(RegistrationRequest registrationRequest);

    AuthorizedResponse login(LoginRequest loginRequest);

    void logout(String token);
}
