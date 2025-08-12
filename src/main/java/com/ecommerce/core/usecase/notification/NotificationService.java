package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for handling notifications across the application.
 * Provides methods for creating, sending, and managing notifications.
 */
public interface NotificationService {

    /**
     * Create a new notification without sending it immediately
     */
    Notification createNotification(Long userId, String subject, String message,
                                    NotificationType type, String recipient);

    /**
     * Send a notification immediately
     */
    Notification sendNotification(Long userId, String subject, String message,
                                  NotificationType type, String recipient);

    /**
     * Send an existing notification
     */
    Notification sendNotification(Notification notification);

    /**
     * Get notifications for a specific user with pagination
     */
    List<Notification> getNotificationsByUserId(Long userId, int page, int size);

    /**
     * Get unread notifications for a user
     */
    List<Notification> getUnreadNotifications(Long userId);

    /**
     * Mark notification as read
     */
    void markAsRead(Long notificationId);

    /**
     * Mark multiple notifications as read
     */
    void markAsRead(List<Long> notificationIds);

    /**
     * Mark all notifications for a user as read
     */
    void markAllAsRead(Long userId);

    /**
     * Delete old notifications (cleanup)
     */
    void deleteOldNotifications(LocalDateTime before);

    /**
     * Get notification by ID
     */
    Optional<Notification> getById(Long id);

    /**
     * Schedule a notification for later delivery
     */
    Notification scheduleNotification(Long userId, String subject, String message,
                                      NotificationType type, String recipient,
                                      LocalDateTime scheduledAt);

    /**
     * Cancel a scheduled notification
     */
    void cancelScheduledNotification(Long notificationId);

    /**
     * Get notifications by type and status
     */
    List<Notification> getNotificationsByTypeAndStatus(NotificationType type,
                                                        NotificationStatus status);

    /**
     * Retry failed notifications
     */
    void retryFailedNotifications();

    // Request DTOs for different notification types

    /**
     * Request object for SMS notifications
     */
    @lombok.Builder
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    class SmsNotificationRequest {
        private String phoneNumber;
        private String message;
        private Map<String, Object> templateVariables;
    }


}
