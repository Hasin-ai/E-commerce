package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.infrastructure.service.NotificationService;
import com.ecommerce.core.usecase.notification.SendNotificationRequest;
import com.ecommerce.core.usecase.notification.EmailNotificationRequest;
import com.ecommerce.core.usecase.notification.PushNotificationRequest;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import com.ecommerce.infrastructure.external.notification.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Override
    public Long sendNotification(Notification notification) {
        log.info("Sending notification to user: {}, type: {}", notification.getUserId(), notification.getType());
        
        // Save the notification first
        Notification savedNotification = notificationRepository.save(notification);
        
        // Send the notification based on type
        try {
            sendNotificationByType(savedNotification);
            savedNotification.markAsSent();
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);
            savedNotification.markAsFailed();
        }
        
        // Update the notification status
        notificationRepository.save(savedNotification);
        
        return savedNotification.getId();
    }

    private void sendNotificationByType(Notification notification) {
        switch (notification.getType()) {
            case EMAIL:
                emailService.sendEmail(EmailNotificationRequest.builder()
                        .to(notification.getRecipient())
                        .subject(notification.getSubject())
                        .message(notification.getMessage())
                        .build());
                break;
            case SMS:
                log.info("SMS notification would be sent to: {}", notification.getRecipient());
                break;
            case PUSH:
                log.info("Push notification would be sent to: {}", notification.getRecipient());
                break;
            default:
                log.warn("Unsupported notification type: {}", notification.getType());
        }
    }
}
