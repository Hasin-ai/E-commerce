package com.ecommerce.adapter.notification;

import com.ecommerce.core.domain.notification.service.EmailService;
import com.ecommerce.core.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String appBaseUrl;

    public EmailServiceImpl(JavaMailSender mailSender,
                          @Value("${app.email.from}") String fromEmail,
                          @Value("${app.base-url}") String appBaseUrl) {
        this.mailSender = mailSender;
        this.fromEmail = fromEmail;
        this.appBaseUrl = appBaseUrl;
    }

    @Override
    public void sendVerificationEmail(User user, String verificationToken) {
        String verificationUrl = String.format("%s/api/auth/verify-email?token=%s", 
            appBaseUrl, verificationToken);
            
        String subject = "Please verify your email address";
        String message = String.format(
            "Hello %s,\n\n" +
            "Thank you for registering with us. Please click the link below to verify your email address:\n" +
            "%s\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "Best regards,\nThe E-commerce Team",
            user.getFirstName(),
            verificationUrl
        );

        sendEmail(user.getEmail().getValue(), subject, message);
    }

    @Override
    public void sendPasswordResetEmail(User user, String resetToken) {
        String resetUrl = String.format("%s/reset-password?token=%s", 
            appBaseUrl, resetToken);
            
        String subject = "Password Reset Request";
        String message = String.format(
            "Hello %s,\n\n" +
            "You have requested to reset your password. Please click the link below to set a new password:\n" +
            "%s\n\n" +
            "This link will expire in 1 hour.\n\n" +
            "If you did not request this, please ignore this email.\n\n" +
            "Best regards,\nThe E-commerce Team",
            user.getFirstName(),
            resetUrl
        );

        sendEmail(user.getEmail().getValue(), subject, message);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        
        mailSender.send(message);
    }
}
