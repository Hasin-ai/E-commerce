package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserUseCase {
    private final UserRepository userRepository;

    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticateUserResponse execute(AuthenticateUserRequest request) {
        Email userEmail = new Email(request.getEmail());
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!user.isActive()) {
            throw new BusinessException("Account is deactivated");
        }

        if (!user.getPassword().matches(request.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        return AuthenticateUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail().getValue())
                .phone(user.getPhone() != null ? user.getPhone().getValue() : null)
                .active(user.isActive())
                .success(true)
                .message("Authentication successful")
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticateUserRequest {
        private String email;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticateUserResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private boolean active;
        private boolean success;
        private String message;
    }
}
