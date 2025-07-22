package com.ecommerce.infrastructure.service;

import com.ecommerce.core.usecase.notification.EmailNotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Email service for sending email notifications.
 * This is a mock implementation - replace with real email service like SendGrid, AWS SES, etc.
 */
@Service
@Slf4j
public class EmailService {

    @Value("${spring.mail.enabled:false}")
    private boolean emailEnabled;

    public void sendEmail(EmailNotificationRequest request) {
        if (!emailEnabled) {
            log.info("Email service disabled. Would send email to: {}, subject: {}",
                    request.getTo(), request.getSubject());
            return;
        }

        try {
            // Mock email sending - replace with real implementation
            log.info("Sending email to: {}", request.getTo());
            log.info("Subject: {}", request.getSubject());
            log.info("Message: {}", request.getMessage());

            // Simulate email sending delay
            Thread.sleep(100);

            log.info("Email sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", request.getTo(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
