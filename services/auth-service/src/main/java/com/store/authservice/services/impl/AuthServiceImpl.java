package com.store.authservice.services.impl;

import com.store.authservice.dto.UserDTO;
import com.store.authservice.dto.UserLoginDTO;
import com.store.authservice.dto.UserRegistrationDTO;
import com.store.authservice.entities.User;
import com.store.authservice.repos.UserRepo;
import com.store.authservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;

    @Autowired
    public AuthServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void register(UserRegistrationDTO userRegistrationDTO) {
        User user = new User(
                userRegistrationDTO.getUsername(),
                userRegistrationDTO.getPassword(),
                userRegistrationDTO.getEmail());

        userRepo.save(user);
    }

    @Override
    public UserDTO login(UserLoginDTO userLoginDTO) {
        User user = userRepo.findByEmail(userLoginDTO.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        userDTO.setStatus(user.getStatus());

        return userDTO;
    }
}
