package com.ecommerce.core.domain.notification.service;

import com.ecommerce.core.domain.user.entity.User;

public interface EmailService {
    void sendVerificationEmail(User user, String verificationToken);
    void sendPasswordResetEmail(User user, String resetToken);
}
