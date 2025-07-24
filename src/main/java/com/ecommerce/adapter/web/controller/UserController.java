package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.UpdateUserRequestDto;
import com.ecommerce.adapter.web.dto.response.UserResponseDto;
import com.ecommerce.core.usecase.user.GetUserUseCase;
import com.ecommerce.core.usecase.user.UpdateUserUseCase;
import com.ecommerce.core.usecase.user.GetUserRequest;
import com.ecommerce.core.usecase.user.UpdateUserRequest;
import com.ecommerce.core.usecase.user.GetUserResponse;
import com.ecommerce.core.usecase.user.UpdateUserResponse;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/users")
@Validated
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UserRepository userRepository;

    public UserController(GetUserUseCase getUserUseCase, UpdateUserUseCase updateUserUseCase, 
                         UserRepository userRepository) {
        this.getUserUseCase = getUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUserProfile(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        GetUserRequest request = new GetUserRequest(userId);
        GetUserResponse response = getUserUseCase.execute(request);
        
        UserResponseDto userDto = mapToUserResponseDto(response);
        
        return ResponseEntity.ok(ApiResponse.success(userDto, "User profile retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable @NotNull @Positive Long id) {
        
        GetUserRequest request = new GetUserRequest(id);
        GetUserResponse response = getUserUseCase.execute(request);
        
        UserResponseDto userDto = mapToUserResponseDto(response);
        
        return ResponseEntity.ok(ApiResponse.success(userDto, "User retrieved successfully"));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateCurrentUserProfile(
            @Valid @RequestBody UpdateUserRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        UpdateUserRequest request = new UpdateUserRequest(
            userId,
            requestDto.getFirstName(),
            requestDto.getLastName(),
            requestDto.getPhone()
        );
        
        UpdateUserResponse response = updateUserUseCase.execute(request);
        
        UserResponseDto userDto = mapToUserResponseDto(response);
        
        return ResponseEntity.ok(ApiResponse.success(userDto, "User profile updated successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable @NotNull @Positive Long id,
            @Valid @RequestBody UpdateUserRequestDto requestDto) {
        
        UpdateUserRequest request = new UpdateUserRequest(
            id,
            requestDto.getFirstName(),
            requestDto.getLastName(),
            requestDto.getPhone()
        );
        
        UpdateUserResponse response = updateUserUseCase.execute(request);
        
        UserResponseDto userDto = mapToUserResponseDto(response);
        
        return ResponseEntity.ok(ApiResponse.success(userDto, "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable @NotNull @Positive Long id) {
        
        // TODO: Implement delete user use case
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateUser(
            @PathVariable @NotNull @Positive Long id) {
        
        // TODO: Implement activate user use case
        return ResponseEntity.ok(ApiResponse.success(null, "User activated successfully"));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDto>> deactivateUser(
            @PathVariable @NotNull @Positive Long id) {
        
        // TODO: Implement deactivate user use case
        return ResponseEntity.ok(ApiResponse.success(null, "User deactivated successfully"));
    }

    private Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(new com.ecommerce.core.domain.user.valueobject.Email(email))
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getId();
    }

    private UserResponseDto mapToUserResponseDto(GetUserResponse response) {
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

    private UserResponseDto mapToUserResponseDto(UpdateUserResponse response) {
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
}