package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.notification.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.usecase.notification.NotificationService;
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
    private final SmsService smsService;

    @Override
    public Notification sendNotification(SendNotificationRequest request) {
        log.info("Sending notification to user: {}, type: {}", request.getUserId(), request.getType());

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .status(Notification.NotificationStatus.PENDING)
                .build();

        notification = notificationRepository.save(notification);

        // Send email notification if email is provided
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            try {
                sendEmailNotification(EmailNotificationRequest.builder()
                        .to(request.getEmail())
                        .subject(request.getTitle())
                        .message(request.getMessage())
                        .templateVariables(request.getTemplateVariables())
                        .build());

                notification.setStatus(Notification.NotificationStatus.SENT);
            } catch (Exception e) {
                log.error("Failed to send email notification: {}", e.getMessage());
                notification.setStatus(Notification.NotificationStatus.FAILED);
                notification.setErrorMessage(e.getMessage());
            }
        }

        // Send SMS notification if phone number is provided
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            try {
                sendSmsNotification(SmsNotificationRequest.builder()
                        .phoneNumber(request.getPhoneNumber())
                        .message(request.getMessage())
                        .templateVariables(request.getTemplateVariables())
                        .build());

                notification.setStatus(Notification.NotificationStatus.SENT);
            } catch (Exception e) {
                log.error("Failed to send SMS notification: {}", e.getMessage());
                notification.setStatus(Notification.NotificationStatus.FAILED);
                notification.setErrorMessage(e.getMessage());
            }
        }

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        notificationRepository.findById(notificationId)
                .filter(notification -> notification.getUserId().equals(userId))
                .ifPresent(notification -> {
                    notification.setRead(true);
                    notification.setReadAt(LocalDateTime.now());
                    notification.setStatus(Notification.NotificationStatus.READ);
                    notificationRepository.save(notification);
                });
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification.setStatus(Notification.NotificationStatus.READ);
        });
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    public void deleteNotification(Long notificationId, Long userId) {
        notificationRepository.findById(notificationId)
                .filter(notification -> notification.getUserId().equals(userId))
                .ifPresent(notificationRepository::delete);
    }

    @Override
    public void sendEmailNotification(EmailNotificationRequest request) {
        emailService.sendEmail(request);
    }

    @Override
    public void sendSmsNotification(SmsNotificationRequest request) {
        smsService.sendSms(request);
    }
}
