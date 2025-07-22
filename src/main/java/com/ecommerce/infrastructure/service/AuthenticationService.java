package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository,
                               PasswordService passwordService,
                               JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtService = jwtService;
    }

    public AuthenticationResult authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(new Email(email));

        if (userOpt.isEmpty()) {
            return AuthenticationResult.failure("Invalid credentials");
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            return AuthenticationResult.failure("Account is deactivated");
        }

        if (!passwordService.matches(password, user.getPassword().getValue())) {
            return AuthenticationResult.failure("Invalid credentials");
        }

        String token = jwtService.generateToken(email);

        return AuthenticationResult.success(user, token);
    }

    public static class AuthenticationResult {
        private final boolean success;
        private final User user;
        private final String token;
        private final String errorMessage;

        private AuthenticationResult(boolean success, User user, String token, String errorMessage) {
            this.success = success;
            this.user = user;
            this.token = token;
            this.errorMessage = errorMessage;
        }

        public static AuthenticationResult success(User user, String token) {
            return new AuthenticationResult(true, user, token, null);
        }

        public static AuthenticationResult failure(String errorMessage) {
            return new AuthenticationResult(false, null, null, errorMessage);
        }

        // Getters
        public boolean isSuccess() { return success; }
        public User getUser() { return user; }
        public String getToken() { return token; }
        public String getErrorMessage() { return errorMessage; }
    }
}
