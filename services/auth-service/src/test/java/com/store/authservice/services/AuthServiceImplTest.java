package com.store.authservice.services;

import com.store.authservice.dto.UserDTO;
import com.store.authservice.dto.requests.LoginRequest;
import com.store.authservice.dto.requests.RegistrationRequest;
import com.store.authservice.dto.responses.AuthorizedResponse;
import com.store.authservice.entities.User;
import com.store.authservice.exceptions.InvalidCredentialsException;
import com.store.authservice.exceptions.UserAlreadyExistsException;
import com.store.authservice.repos.UserRepo;
import com.store.authservice.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldCreateUserAndSession() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("maxim");
        request.setEmail("maxim@test.com");
        request.setPassword("password123");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepo.existsByEmail("maxim@test.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("hashed-password");

        when(userRepo.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        when(sessionService.createSession(any(UserDTO.class)))
                .thenReturn("session-token");

        AuthorizedResponse response = authService.register(request);

        assertEquals("session-token", response.getToken());

        verify(passwordEncoder).encode("password123");

        verify(userRepo).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("maxim", savedUser.getUsername());
        assertEquals("maxim@test.com", savedUser.getEmail());
        assertEquals("hashed-password", savedUser.getPassword());
        assertNotEquals("password123", savedUser.getPassword());

        verify(sessionService).createSession(any(UserDTO.class));
    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowException() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("maxim@test.com");

        when(userRepo.existsByEmail("maxim@test.com"))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        verify(userRepo, never()).save(any());
        verify(sessionService, never()).createSession(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void login_withValidCredentials_shouldCreateSession() {
        LoginRequest request = new LoginRequest();
        request.setEmail("maxim@test.com");
        request.setPassword("password123");

        User user = new User(
                "maxim",
                "hashed-password",
                "maxim@test.com"
        );
        user.setId(1L);

        when(userRepo.findByEmail("maxim@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "hashed-password"
        )).thenReturn(true);

        when(sessionService.createSession(any(UserDTO.class)))
                .thenReturn("token123");

        AuthorizedResponse response = authService.login(request);

        assertEquals("token123", response.getToken());

        verify(sessionService).createSession(any(UserDTO.class));
    }

    @Test
    void login_whenUserDoesNotExist_shouldThrowInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@test.com");

        when(userRepo.findByEmail("unknown@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(sessionService, never()).createSession(any());
    }

    @Test
    void login_whenPasswordIsIncorrect_shouldThrowInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("maxim@test.com");
        request.setPassword("wrong");

        User user = new User(
                "maxim",
                "hashed-password",
                "maxim@test.com"
        );

        when(userRepo.findByEmail("maxim@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrong",
                "hashed-password"
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(sessionService, never()).createSession(any());
    }

    @Test
    void logout_shouldDeleteSession() {
        authService.logout("token123");

        verify(sessionService).logout("token123");
    }
}