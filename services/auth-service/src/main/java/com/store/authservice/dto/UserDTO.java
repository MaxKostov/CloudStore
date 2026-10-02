package com.store.authservice.dto;

import com.store.authservice.entities.enums.UserRole;
import com.store.authservice.entities.enums.UserStatus;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private UserStatus status;
}
