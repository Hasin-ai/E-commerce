package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.Password;
import com.ecommerce.core.domain.user.valueobject.Phone;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterUserResponse execute(RegisterUserRequest request) {
        // Validate email is not already registered
        Email email = new Email(request.getEmail());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new com.ecommerce.shared.exception.DuplicateEmailException("Email already registered");
        }

        // Validate password before encoding
        new Password(request.getPassword()); // This validates the plain password
        
        // Create new user with encoded password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getEmail(), request.getFirstName(), request.getLastName(), request.getPhone());
        user.setEncodedPassword(encodedPassword);
        
        // For testing purposes, automatically activate the user
        user.activateAccount();
        user.setEmailVerified(true);

        User savedUser = userRepository.save(user);

        return RegisterUserResponse.builder()
            .id(savedUser.getId())
            .firstName(savedUser.getFirstName())
            .lastName(savedUser.getLastName())
            .email(savedUser.getEmail().getValue())
            .phone(savedUser.getPhone() != null ? savedUser.getPhone().getValue() : null)
            .isActive(savedUser.isActive())
            .isEmailVerified(savedUser.isEmailVerified())
            .createdAt(savedUser.getCreatedAt())
            .updatedAt(savedUser.getUpdatedAt())
            .build();
    }
}