package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.infrastructure.security.JwtTokenProvider;
import com.ecommerce.shared.exception.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginUserResponse execute(LoginUserRequest request) {
        // Find user by email
        Email email = new Email(request.getEmail());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword().getValue())) {
            throw new AuthenticationException("Invalid credentials");
        }

        // Check if user is active
        if (!user.isActive()) {
            throw new AuthenticationException("Account is deactivated");
        }

        // Generate JWT token
        String accessToken = jwtTokenProvider.generateToken(user.getEmail().getValue());
        long expiresIn = jwtTokenProvider.getExpirationTime();

        LoginUserResponse.UserInfo userInfo = new LoginUserResponse.UserInfo(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail().getValue(),
            user.getPhone() != null ? user.getPhone().getValue() : null,
            user.isActive(),
            user.isEmailVerified(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );

        return new LoginUserResponse(accessToken, expiresIn, userInfo);
    }
}