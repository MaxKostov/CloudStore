package com.store.authservice.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
