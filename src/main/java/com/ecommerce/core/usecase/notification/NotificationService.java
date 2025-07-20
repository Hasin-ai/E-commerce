package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.Notification;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing notifications.
 */
public interface NotificationService {

    /**
     * Send a notification to a user.
     */
    Notification sendNotification(SendNotificationRequest request);

    /**
     * Get all notifications for a user.
     */
    List<Notification> getUserNotifications(Long userId);

    /**
     * Get unread notifications for a user.
     */
    List<Notification> getUnreadNotifications(Long userId);

    /**
     * Mark a notification as read.
     */
    void markAsRead(Long notificationId, Long userId);

    /**
     * Mark all notifications as read for a user.
     */
    void markAllAsRead(Long userId);

    /**
     * Get unread notification count for a user.
     */
    Long getUnreadCount(Long userId);

    /**
     * Delete a notification.
     */
    void deleteNotification(Long notificationId, Long userId);

    /**
     * Send email notification.
     */
    void sendEmailNotification(EmailNotificationRequest request);

    /**
     * Send SMS notification.
     */
    void sendSmsNotification(SmsNotificationRequest request);

    @Data
    @Builder
    class SendNotificationRequest {
        private Long userId;
        private Notification.NotificationType type;
        private String title;
        private String message;
        private String email;
        private String phoneNumber;
        private Map<String, String> templateVariables;
    }

    @Data
    @Builder
    class EmailNotificationRequest {
        private String to;
        private String subject;
        private String message;
        private String templateName;
        private Map<String, String> templateVariables;
    }

    @Data
    @Builder
    class SmsNotificationRequest {
        private String phoneNumber;
        private String message;
        private String templateName;
        private Map<String, String> templateVariables;
    }
}
