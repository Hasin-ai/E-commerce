package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.Password;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.infrastructure.service.PasswordService;

import java.time.LocalDateTime;

public class RegisterUserUseCase {
    
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public RegisterUserUseCase(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    // Constructor without PasswordService for backward compatibility
    public RegisterUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordService = null;
    }
    
    public RegisterUserResponse execute(RegisterUserRequest request) {
        // Validate input
        validateRequest(request);
        
        // Check business rules
        Email email = new Email(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("User with email " + email.getValue() + " already exists");
        }
        
        // Encode password if service is available
        String encodedPassword = passwordService != null ?
            passwordService.encodePassword(request.getPassword()) :
            request.getPassword();

        // Create domain entity
        Password password = new Password(encodedPassword);
        User user = new User(
            request.getFirstName(),
            request.getLastName(),
            email,
            password,
            request.getPhone()
        );
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Return response
        return new RegisterUserResponse(
            savedUser.getId(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getEmail().getValue(),
            savedUser.getPhone(),
            savedUser.isActive(),
            savedUser.isEmailVerified(),
            savedUser.getCreatedAt()
        );
    }
    
    private void validateRequest(RegisterUserRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new BusinessException("First name is required");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new BusinessException("Last name is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BusinessException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException("Password is required");
        }
    }
    
    // Request and Response classes
    public static class RegisterUserRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String phone;
        
        public RegisterUserRequest() {}
        
        public RegisterUserRequest(String firstName, String lastName, String email, String password, String phone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.password = password;
            this.phone = phone;
        }
        
        // Getters and setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
    
    public static class RegisterUserResponse {
        private final Long id;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String phone;
        private final boolean isActive;
        private final boolean isEmailVerified;
        private final LocalDateTime createdAt;
        
        public RegisterUserResponse(Long id, String firstName, String lastName, String email,
                                  String phone, boolean isActive, boolean isEmailVerified,
                                  LocalDateTime createdAt) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.phone = phone;
            this.isActive = isActive;
            this.isEmailVerified = isEmailVerified;
            this.createdAt = createdAt;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public boolean isActive() { return isActive; }
        public boolean isEmailVerified() { return isEmailVerified; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
}
