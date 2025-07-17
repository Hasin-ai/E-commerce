package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.RegisterUserRequestDto;
import com.ecommerce.adapter.web.dto.request.LoginRequestDto;
import com.ecommerce.adapter.web.dto.response.UserResponseDto;
import com.ecommerce.adapter.web.dto.response.AuthResponseDto;
import com.ecommerce.adapter.web.mapper.UserMapper;
import com.ecommerce.core.usecase.user.RegisterUserUseCase;
import com.ecommerce.core.usecase.user.AuthenticateUserUseCase;
import com.ecommerce.shared.dto.ApiResponse;
import com.ecommerce.infrastructure.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(RegisterUserUseCase registerUserUseCase,
                         AuthenticateUserUseCase authenticateUserUseCase,
                         UserMapper userMapper,
                         JwtTokenProvider jwtTokenProvider) {
        this.registerUserUseCase = registerUserUseCase;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(
            @Valid @RequestBody RegisterUserRequestDto requestDto) {

        RegisterUserUseCase.RegisterUserRequest request =
            userMapper.toRegisterUserRequest(requestDto);

        RegisterUserUseCase.RegisterUserResponse response =
            registerUserUseCase.execute(request);

        UserResponseDto responseDto = userMapper.toUserResponse(response);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(responseDto, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(
            @Valid @RequestBody LoginRequestDto requestDto) {

        AuthenticateUserUseCase.AuthenticateUserRequest request =
            userMapper.toAuthenticateUserRequest(requestDto);

        AuthenticateUserUseCase.AuthenticateUserResponse response =
            authenticateUserUseCase.execute(request);

        // Generate JWT token
        String accessToken = jwtTokenProvider.generateToken(response.getEmail());
        long expiresIn = jwtTokenProvider.getExpirationTime();

        AuthResponseDto responseDto = userMapper.toAuthResponse(accessToken, expiresIn, response);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Login successful"));
    }
}
