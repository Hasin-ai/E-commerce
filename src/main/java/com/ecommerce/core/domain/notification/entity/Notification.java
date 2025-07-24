package com.ecommerce.core.domain.notification.entity;

import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String recipient;
    private String subject;
    private NotificationType type;
    private NotificationChannel channel;
    private NotificationStatus status;
    private Map<String, Object> data;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private LocalDateTime scheduledAt;
    private int retryCount;
    private int maxRetries;

    public boolean isRead() {
        return status == NotificationStatus.READ;
    }

    public void setRead(boolean read) {
        if (read) {
            this.status = NotificationStatus.READ;
            this.readAt = LocalDateTime.now();
        }
    }

    public void markAsRead() {
        setRead(true);
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }

    public void schedule(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
        this.status = NotificationStatus.SCHEDULED;
    }

    public boolean isScheduled() {
        return status == NotificationStatus.SCHEDULED;
    }

    public boolean canRetry() {
        return status == NotificationStatus.FAILED && retryCount < maxRetries;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }
}