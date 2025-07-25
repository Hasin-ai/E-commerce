package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.request.RegisterUserRequestDto;
import com.ecommerce.adapter.web.dto.request.LoginRequestDto;
import com.ecommerce.adapter.web.dto.response.UserResponseDto;
import com.ecommerce.adapter.web.dto.response.AuthResponseDto;
import com.ecommerce.core.usecase.user.RegisterUserRequest;
import com.ecommerce.core.usecase.user.RegisterUserResponse;
import com.ecommerce.core.usecase.user.GetUserResponse;
import com.ecommerce.core.usecase.user.AuthenticateUserUseCase;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public RegisterUserRequest toRegisterUserRequest(RegisterUserRequestDto dto) {
        return RegisterUserRequest.builder()
            .firstName(dto.getFirstName())
            .lastName(dto.getLastName())
            .email(dto.getEmail())
            .password(dto.getPassword())
            .phone(dto.getPhone())
            .build();
    }

    public AuthenticateUserUseCase.AuthenticateUserRequest toAuthenticateUserRequest(LoginRequestDto dto) {
        return new AuthenticateUserUseCase.AuthenticateUserRequest(
            dto.getEmail(),
            dto.getPassword()
        );
    }

    public UserResponseDto toUserResponse(RegisterUserResponse response) {
        return UserResponseDto.builder()
            .id(response.getId())
            .firstName(response.getFirstName())
            .lastName(response.getLastName())
            .email(response.getEmail())
            .phone(response.getPhone())
            .isActive(response.isActive())
            .isEmailVerified(response.isEmailVerified())
            .createdAt(response.getCreatedAt())
            .updatedAt(response.getUpdatedAt())
            .build();
    }

    public UserResponseDto toUserResponse(GetUserResponse response) {
        return UserResponseDto.builder()
            .id(response.getId())
            .firstName(response.getFirstName())
            .lastName(response.getLastName())
            .email(response.getEmail())
            .phone(response.getPhone())
            .isActive(response.isActive())
            .isEmailVerified(false) // GetUserResponse doesn't have this field
            .createdAt(response.getCreatedAt())
            .updatedAt(response.getUpdatedAt())
            .build();
    }

    public UserResponseDto toUserResponse(AuthenticateUserUseCase.AuthenticateUserResponse response) {
        return new UserResponseDto(
            response.getId(),
            response.getFirstName(),
            response.getLastName(),
            response.getEmail(),
            response.getPhone(),
            response.isActive(),
            true, // default email verification status for authenticated users
            null, // createdAt not included in auth response
            null  // updatedAt not included in auth response
        );
    }

    public AuthResponseDto toAuthResponse(String accessToken, long expiresIn, AuthenticateUserUseCase.AuthenticateUserResponse userResponse) {
        UserResponseDto userDto = toUserResponse(userResponse);
        return new AuthResponseDto(accessToken, "Bearer", expiresIn, userDto);
    }
}