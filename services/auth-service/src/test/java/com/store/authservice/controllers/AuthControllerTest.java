package com.store.authservice.controllers;

import com.store.authservice.dto.requests.LoginRequest;
import com.store.authservice.dto.responses.AuthorizedResponse;
import com.store.authservice.exceptions.InvalidCredentialsException;
import com.store.authservice.exceptions.UserAlreadyExistsException;
import com.store.authservice.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockitoBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void login_shouldReturnToken() throws Exception {
        AuthorizedResponse response = new AuthorizedResponse();
        response.setToken("token123");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "email": "maxim@test.com",
                                      "password": "password123"
                                    }
                                 """))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    void logout_shouldReturn204() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "token123"))
                .andExpect(status().isNoContent());

        verify(authService).logout("token123");
    }

    @Test
    void register_whenUserAlreadyExists_shouldReturn409() throws Exception {
        when(authService.register(any()))
                .thenThrow(new UserAlreadyExistsException());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "maxim",
                          "email": "maxim@test.com",
                          "password": "Password1!"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        when(authService.login(any()))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                      "email": "maxim@test.com",
                                      "password": "wrong"
                                    }
                                 """))
                        .andExpect(status().isUnauthorized())
                        .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void register_withInvalidPassword_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "maxim",
                          "email": "maxim@test.com",
                          "password": "Pass!"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());

        verify(authService, never()).register(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Pass!",
            "password!",
            "Password123",
            "Password123456789!"
    })
    void register_withInvalidPassword_shouldReturn400(
            String password
    ) throws Exception {

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "maxim",
                          "email": "maxim@test.com",
                          "password": "%s"
                        }
                        """.formatted(password)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }
}
