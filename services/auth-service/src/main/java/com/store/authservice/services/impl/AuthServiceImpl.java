package com.store.authservice.services.impl;

import com.store.authservice.dto.AuthorizedResponse;
import com.store.authservice.dto.UserDTO;
import com.store.authservice.dto.LoginRequest;
import com.store.authservice.dto.RegistrationRequest;
import com.store.authservice.entities.User;
import com.store.authservice.repos.UserRepo;
import com.store.authservice.services.AuthService;
import com.store.authservice.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    @Autowired
    public AuthServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, SessionService sessionService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
    }

    @Override
    public AuthorizedResponse register(RegistrationRequest registrationRequest) {
        if (userRepo.existsByEmail(registrationRequest.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User(
                registrationRequest.getUsername(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                registrationRequest.getEmail());

        userRepo.save(user);

        UserDTO userDTO = toUserDTO(user);
        String token = sessionService.createSession(userDTO);
        AuthorizedResponse authorizedResponse = new AuthorizedResponse();
        authorizedResponse.setToken(token);

        return authorizedResponse;
    }

    @Override
    public AuthorizedResponse login(LoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getEmail())
                        .orElseThrow(() -> new RuntimeException("Invalid email or password"));


        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        UserDTO userDTO = toUserDTO(user);

        String token = sessionService.createSession(userDTO);
        AuthorizedResponse authorizedResponse = new AuthorizedResponse();
        authorizedResponse.setToken(token);

        return authorizedResponse;
    }

    @Override
    public void logout(String token) {
        sessionService.logout(token);
    }

    private UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setId(user.getId());
        userDTO.setRole(user.getRole());
        userDTO.setStatus(user.getStatus());
        return userDTO;
    }

}
