package com.store.authservice.services;

import com.store.authservice.dto.UserDTO;
import com.store.authservice.dto.UserLoginDTO;
import com.store.authservice.dto.UserRegistrationDTO;

public interface AuthService {
    void register(UserRegistrationDTO userRegistrationDTO);

    UserDTO login(UserLoginDTO userLoginDTO);
}
