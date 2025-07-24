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
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        Password encodedPassword = new Password(passwordEncoder.encode(request.getPassword()));
        Phone phone = request.getPhone() != null ? new Phone(request.getPhone()) : null;

        User user = new User(
            request.getFirstName(),
            request.getLastName(),
            email,
            encodedPassword,
            phone
        );

        User savedUser = userRepository.save(user);

        return new RegisterUserResponse(
            savedUser.getId(),
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getEmail().getValue(),
            savedUser.getPhone() != null ? savedUser.getPhone().getValue() : null,
            savedUser.isActive(),
            savedUser.isEmailVerified(),
            savedUser.getCreatedAt(),
            savedUser.getUpdatedAt()
        );
    }
}