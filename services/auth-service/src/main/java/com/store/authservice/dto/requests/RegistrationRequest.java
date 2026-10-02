package com.store.authservice.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[^A-Za-z0-9]).*$",
            message = "Password must contain at least one uppercase letter and one special character"
    )
    private String password;

    @NotBlank
    @Email
    private String email;
}
