package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.response.UserResponseDto;
import com.ecommerce.adapter.web.mapper.UserMapper;
import com.ecommerce.core.usecase.user.GetUserUseCase;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final UserMapper userMapper;

    @Autowired
    public UserController(GetUserUseCase getUserUseCase, UserMapper userMapper) {
        this.getUserUseCase = getUserUseCase;
        this.userMapper = userMapper;
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();

        GetUserUseCase.GetUserResponse response = getUserUseCase.executeByEmail(email);
        UserResponseDto responseDto = userMapper.toUserResponse(response);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable @NotNull @Positive Long id) {

        GetUserUseCase.GetUserResponse response = getUserUseCase.execute(id);
        UserResponseDto responseDto = userMapper.toUserResponse(response);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}
