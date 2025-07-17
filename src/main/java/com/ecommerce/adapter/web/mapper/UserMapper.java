package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.request.RegisterUserRequestDto;
import com.ecommerce.adapter.web.dto.request.LoginRequestDto;
import com.ecommerce.adapter.web.dto.response.UserResponseDto;
import com.ecommerce.adapter.web.dto.response.AuthResponseDto;
import com.ecommerce.core.usecase.user.RegisterUserUseCase;
import com.ecommerce.core.usecase.user.AuthenticateUserUseCase;
import com.ecommerce.core.usecase.user.GetUserUseCase;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public RegisterUserUseCase.RegisterUserRequest toRegisterUserRequest(RegisterUserRequestDto dto) {
        return new RegisterUserUseCase.RegisterUserRequest(
            dto.getFirstName(),
            dto.getLastName(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getPhone()
        );
    }

    public AuthenticateUserUseCase.AuthenticateUserRequest toAuthenticateUserRequest(LoginRequestDto dto) {
        return new AuthenticateUserUseCase.AuthenticateUserRequest(
            dto.getEmail(),
            dto.getPassword()
        );
    }

    public UserResponseDto toUserResponse(RegisterUserUseCase.RegisterUserResponse response) {
        return new UserResponseDto(
            response.getId(),
            response.getFirstName(),
            response.getLastName(),
            response.getEmail(),
            response.getPhone(),
            response.isActive(),
            response.isEmailVerified(),
            response.getCreatedAt(),
            response.getCreatedAt() // updatedAt is same as createdAt initially
        );
    }

    public UserResponseDto toUserResponse(GetUserUseCase.GetUserResponse response) {
        return new UserResponseDto(
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
    }

    public UserResponseDto toUserResponse(AuthenticateUserUseCase.AuthenticateUserResponse response) {
        return new UserResponseDto(
            response.getId(),
            response.getFirstName(),
            response.getLastName(),
            response.getEmail(),
            response.getPhone(),
            response.isActive(),
            response.isEmailVerified(),
            null, // createdAt not included in auth response
            null  // updatedAt not included in auth response
        );
    }

    public AuthResponseDto toAuthResponse(String accessToken, long expiresIn, AuthenticateUserUseCase.AuthenticateUserResponse userResponse) {
        UserResponseDto userDto = toUserResponse(userResponse);
        return new AuthResponseDto(accessToken, expiresIn, userDto);
    }
}
