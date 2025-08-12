package com.ecommerce.core.domain.auth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.core.domain.user.entity.User;

@Service
public class TokenService {

    private final long verificationTokenExpirationHours;
    private final long passwordResetTokenExpirationMinutes;

    public TokenService(
            @Value("${app.email.verification-expiration-hours:24}") long verificationTokenExpirationHours,
            @Value("${app.email.password-reset-expiration-minutes:60}") long passwordResetTokenExpirationMinutes) {
        this.verificationTokenExpirationHours = verificationTokenExpirationHours;
        this.passwordResetTokenExpirationMinutes = passwordResetTokenExpirationMinutes;
    }

    public String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    public String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }

    public LocalDateTime calculateVerificationExpiryDate() {
        return LocalDateTime.now().plusHours(verificationTokenExpirationHours);
    }

    public LocalDateTime calculatePasswordResetExpiryDate() {
        return LocalDateTime.now().plusMinutes(passwordResetTokenExpirationMinutes);
    }

    public boolean isTokenExpired(LocalDateTime expiryDate) {
        return expiryDate == null || LocalDateTime.now().isAfter(expiryDate);
    }
}
