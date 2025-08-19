package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.user.RegisterUserUseCase;
import com.ecommerce.core.usecase.user.LoginUserUseCase;
import com.ecommerce.core.usecase.user.RegisterUserRequest;
import com.ecommerce.core.usecase.user.RegisterUserResponse;
import com.ecommerce.core.usecase.user.LoginUserRequest;
import com.ecommerce.core.usecase.user.LoginUserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private LoginUserUseCase loginUserUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterUserRequest registerRequest;
    private LoginUserRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterUserRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");

        loginRequest = new LoginUserRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        RegisterUserResponse response = new RegisterUserResponse();
        response.setSuccess(true);
        response.setUserId(1L);
        response.setMessage("User registered successfully");

        when(registerUserUseCase.execute(any(RegisterUserRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    @DisplayName("Should handle registration failure")
    void shouldHandleRegistrationFailure() throws Exception {
        // Given
        RegisterUserResponse response = new RegisterUserResponse();
        response.setSuccess(false);
        response.setErrorMessage("Username already exists");

        when(registerUserUseCase.execute(any(RegisterUserRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Username already exists"));
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        // Given
        LoginUserResponse response = new LoginUserResponse();
        response.setSuccess(true);
        response.setUserId(1L);
        response.setAccessToken("jwt-token");
        response.setMessage("Login successful");

        when(loginUserUseCase.execute(any(LoginUserRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    @DisplayName("Should handle login failure")
    void shouldHandleLoginFailure() throws Exception {
        // Given
        LoginUserResponse response = new LoginUserResponse();
        response.setSuccess(false);
        response.setErrorMessage("Invalid credentials");

        when(loginUserUseCase.execute(any(LoginUserRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Invalid credentials"));
    }

    @Test
    @DisplayName("Should validate request body")
    void shouldValidateRequestBody() throws Exception {
        // Given
        RegisterUserRequest invalidRequest = new RegisterUserRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
