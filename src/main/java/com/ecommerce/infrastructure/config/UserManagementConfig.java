package com.ecommerce.infrastructure.config;

import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.usecase.user.AuthenticateUserUseCase;
import com.ecommerce.core.usecase.user.GetUserUseCase;
import com.ecommerce.core.usecase.user.RegisterUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserManagementConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository) {
        return new RegisterUserUseCase(userRepository);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository) {
        return new AuthenticateUserUseCase(userRepository);
    }

    @Bean
    public GetUserUseCase getUserUseCase(UserRepository userRepository) {
        return new GetUserUseCase(userRepository);
    }
}