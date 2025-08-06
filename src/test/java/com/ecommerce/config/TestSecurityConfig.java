package com.ecommerce.config;

import com.ecommerce.infrastructure.security.JwtTokenProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider("testSecretKeyForJWTTtestSecretKeyForJWTTtestSecretKeyForJWTT", 86400000L);
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return User.builder()
                        .username(username)
                        .password("password")
                        .authorities("USER")
                        .build();
            }
        };
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}