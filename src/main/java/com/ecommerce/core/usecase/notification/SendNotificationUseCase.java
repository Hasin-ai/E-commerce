package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendNotificationUseCase {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public Notification execute(NotificationService.SendNotificationRequest request) {
        log.info("Sending notification to user: {}, type: {}", request.getUserId(), request.getType());

        // Create notification entity
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .status(Notification.NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // Save notification to database
        notification = notificationRepository.save(notification);

        try {
            // Send email notification if email is provided
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                notificationService.sendEmailNotification(
                        NotificationService.EmailNotificationRequest.builder()
                                .to(request.getEmail())
                                .subject(request.getTitle())
                                .message(request.getMessage())
                                .templateVariables(request.getTemplateVariables())
                                .build()
                );
            }

            // Send SMS notification if phone number is provided
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                notificationService.sendSmsNotification(
                        NotificationService.SmsNotificationRequest.builder()
                                .phoneNumber(request.getPhoneNumber())
                                .message(request.getMessage())
                                .templateVariables(request.getTemplateVariables())
                                .build()
                );
            }

            // Update notification status to sent
            notification.setStatus(Notification.NotificationStatus.SENT);
            notification = notificationRepository.save(notification);

            log.info("Successfully sent notification with ID: {}", notification.getId());

        } catch (Exception e) {
            log.error("Failed to send notification with ID: {}", notification.getId(), e);
            notification.setStatus(Notification.NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
            notificationRepository.save(notification);
        }

        return notification;
    }
}
