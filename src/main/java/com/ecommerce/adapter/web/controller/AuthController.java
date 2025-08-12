package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.LoginRequestDto;
import com.ecommerce.adapter.web.dto.request.RegisterUserRequestDto;
import com.ecommerce.adapter.web.dto.response.AuthResponseDto;
import com.ecommerce.adapter.web.dto.response.UserResponseDto;
import com.ecommerce.core.usecase.user.RegisterUserUseCase;
import com.ecommerce.core.usecase.user.LoginUserUseCase;
import com.ecommerce.core.usecase.user.RegisterUserRequest;
import com.ecommerce.core.usecase.user.LoginUserRequest;
import com.ecommerce.core.usecase.user.RegisterUserResponse;
import com.ecommerce.core.usecase.user.LoginUserResponse;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(
            @Valid @RequestBody RegisterUserRequestDto requestDto) {
        
        RegisterUserRequest request = new RegisterUserRequest(
            requestDto.getFirstName(),
            requestDto.getLastName(),
            requestDto.getEmail(),
            requestDto.getPassword(),
            requestDto.getPhone()
        );
        
        RegisterUserResponse response = registerUserUseCase.execute(request);
        
        UserResponseDto userDto = new UserResponseDto(
            response.getId(),
            response.getFirstName(),
            response.getLastName(),
            response.getEmail(),
            response.getPhone(),
            response.isActive(),
            response.isEmailVerified(),
            response.getCreatedAt(),
            response.getUpdatedAt()
        );
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(userDto, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto) {
        
        LoginUserRequest request = new LoginUserRequest(
            requestDto.getEmail(),
            requestDto.getPassword()
        );
        
        LoginUserResponse response = loginUserUseCase.execute(request);
        
        UserResponseDto userDto = new UserResponseDto(
            response.getUser().getId(),
            response.getUser().getFirstName(),
            response.getUser().getLastName(),
            response.getUser().getEmail(),
            response.getUser().getPhone(),
            response.getUser().isActive(),
            response.getUser().isEmailVerified(),
            response.getUser().getCreatedAt(),
            response.getUser().getUpdatedAt()
        );
        
        AuthResponseDto authDto = new AuthResponseDto(
            response.getAccessToken(),
            "Bearer",
            response.getExpiresIn(),
            userDto
        );
        
        return ResponseEntity.ok(ApiResponse.success(authDto, "Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // For JWT tokens, logout is typically handled client-side by removing the token
        // However, we provide this endpoint for consistency and future token blacklisting if needed
        return ResponseEntity.ok(ApiResponse.success(null, "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponseDto>> refreshToken(
            @RequestHeader("Authorization") String refreshToken) {
        
        // TODO: Implement refresh token logic
        return ResponseEntity.ok(ApiResponse.success(null, "Token refreshed successfully"));
    }
}