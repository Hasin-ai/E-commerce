package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.Password;
import com.ecommerce.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisterUserResponse execute(RegisterUserRequest request) {
        Email userEmail = new Email(request.getEmail());

        if (userRepository.existsByEmail(userEmail)) {
            throw new BusinessException("User with this email already exists");
        }

        User user = new User(request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName(), request.getPhone());

        User savedUser = userRepository.save(user);

        return RegisterUserResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail().getValue())
                .phone(savedUser.getPhone())
                .success(true)
                .message("User registered successfully")
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterUserRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String phone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterUserResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private boolean success;
        private String message;
    }
}
