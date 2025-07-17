package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.infrastructure.service.PasswordService;

public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AuthenticateUserUseCase(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    // Constructor without PasswordService for backward compatibility
    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordService = null;
    }

    public AuthenticateUserResponse execute(AuthenticateUserRequest request) {
        validateRequest(request);

        Email email = new Email(request.getEmail());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!user.isActive()) {
            throw new BusinessException("User account is deactivated");
        }

        // Verify password if service is available
        if (passwordService != null) {
            boolean passwordMatches = passwordService.matches(request.getPassword(), user.getPassword().getValue());
            if (!passwordMatches) {
                throw new BusinessException("Invalid email or password");
            }
        }

        return new AuthenticateUserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail().getValue(),
            user.getPhone(),
            user.isActive(),
            user.isEmailVerified()
        );
    }

    private void validateRequest(AuthenticateUserRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BusinessException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException("Password is required");
        }
    }

    public static class AuthenticateUserRequest {
        private String email;
        private String password;

        public AuthenticateUserRequest() {}

        public AuthenticateUserRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthenticateUserResponse {
        private final Long id;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phone;
        private final boolean isActive;
        private final boolean isEmailVerified;

        public AuthenticateUserResponse(Long id, String firstName, String lastName, String email,
                                      String phone, boolean isActive, boolean isEmailVerified) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.isActive = isActive;
            this.isEmailVerified = isEmailVerified;
        }

        public Long getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public boolean isActive() { return isActive; }
        public boolean isEmailVerified() { return isEmailVerified; }
    }
}
