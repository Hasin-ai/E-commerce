package com.ecommerce.infrastructure.config;

import com.ecommerce.adapter.persistence.implementation.UserRepositoryImpl;
import com.ecommerce.adapter.persistence.mapper.UserEntityMapper;
import com.ecommerce.adapter.persistence.jpa.repository.UserJpaRepository;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.usecase.user.RegisterUserUseCase;
import com.ecommerce.core.usecase.user.AuthenticateUserUseCase;
import com.ecommerce.core.usecase.user.GetUserUseCase;
import com.ecommerce.infrastructure.service.PasswordService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserManagementConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository,
                                       UserEntityMapper userEntityMapper) {
        return new UserRepositoryImpl(userJpaRepository, userEntityMapper);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository,
                                                 PasswordService passwordService) {
        return new RegisterUserUseCase(userRepository, passwordService);
    }

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository,
                                                         PasswordService passwordService) {
        return new AuthenticateUserUseCase(userRepository, passwordService);
    }

    @Bean
    public GetUserUseCase getUserUseCase(UserRepository userRepository) {
        return new GetUserUseCase(userRepository);
    }
}
