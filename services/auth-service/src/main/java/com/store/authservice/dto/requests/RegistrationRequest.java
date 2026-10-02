package com.store.authservice.dto.requests;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
}
