package com.ecommerce.infrastructure.service;

import com.ecommerce.core.usecase.notification.NotificationService.SmsNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SMS service for sending SMS notifications.
 * This is a mock implementation - replace with real SMS service like Twilio, AWS SNS, etc.
 */
@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    @Value("${spring.sms.enabled:false}")
    private boolean smsEnabled;

    public void sendSms(SmsNotificationRequest request) {
        if (!smsEnabled) {
            log.info("SMS service disabled. Would send SMS to: {}, message: {}",
                    request.getPhoneNumber(), request.getMessage());
            return;
        }

        try {
            // Mock SMS sending - replace with real implementation
            log.info("Sending SMS to: {}", request.getPhoneNumber());
            log.info("Message: {}", request.getMessage());

            // Simulate SMS sending delay
            Thread.sleep(50);

            log.info("SMS sent successfully to: {}", request.getPhoneNumber());
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", request.getPhoneNumber(), e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }
}
