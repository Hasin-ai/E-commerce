package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.usecase.notification.NotificationService;
import com.ecommerce.core.usecase.notification.SendNotificationRequest;
import com.ecommerce.core.usecase.notification.EmailNotificationRequest;
import com.ecommerce.core.usecase.notification.PushNotificationRequest;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import com.ecommerce.infrastructure.service.EmailService;
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
    public Notification createNotification(Long userId, String subject, String message,
                                           NotificationType type, String recipient) {
        log.info("Creating notification for user: {}, type: {}", userId, type);
        
        Notification notification = new Notification(userId, type, subject, message, recipient);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification sendNotification(Long userId, String subject, String message,
                                         NotificationType type, String recipient) {
        log.info("Sending notification to user: {}, type: {}", userId, type);

        Notification notification = new Notification(userId, type, subject, message, recipient);
        notification = notificationRepository.save(notification);

        return sendNotification(notification);
    }

    @Override
    public Notification sendNotification(Notification notification) {
        log.info("Sending existing notification: {}", notification.getId());
        
        try {
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
            notification.markAsSent();
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            notification.markAsFailed();
        }

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUserId(Long userId, int page, int size) {
        // For now, return all notifications for the user (pagination can be added later)
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.markAsRead();
                    notificationRepository.save(notification);
                });
    }

    @Override
    public void markAsRead(List<Long> notificationIds) {
        notificationIds.forEach(this::markAsRead);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        unreadNotifications.forEach(notification -> {
            notification.markAsRead();
        });
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void deleteOldNotifications(LocalDateTime before) {
        // Implementation would delete notifications created before the given date
        log.info("Deleting notifications created before: {}", before);
        // notificationRepository.deleteByCreatedAtBefore(before);
    }

    @Override
    public java.util.Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Notification scheduleNotification(Long userId, String subject, String message,
                                             NotificationType type, String recipient,
                                             LocalDateTime scheduledAt) {
        log.info("Scheduling notification for user: {}, type: {}, scheduled at: {}", userId, type, scheduledAt);
        
        Notification notification = new Notification(userId, type, subject, message, recipient);
        notification.schedule(scheduledAt);
        return notificationRepository.save(notification);
    }

    @Override
    public void cancelScheduledNotification(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    if (notification.isScheduled()) {
                        notification.setStatus(NotificationStatus.CANCELLED);
                        notificationRepository.save(notification);
                    }
                });
    }

    @Override
    public List<Notification> getNotificationsByTypeAndStatus(NotificationType type,
                                                               NotificationStatus status) {
        return notificationRepository.findByTypeAndStatus(type, status);
    }

    @Override
    public void retryFailedNotifications() {
        List<Notification> failedNotifications = notificationRepository.findByStatusAndCreatedAtBefore(
                NotificationStatus.FAILED, LocalDateTime.now().minusHours(1));
        
        failedNotifications.stream()
                .filter(Notification::canRetry)
                .forEach(notification -> {
                    notification.incrementRetryCount();
                    sendNotification(notification);
                });
    }
}
