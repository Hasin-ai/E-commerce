package com.ecommerce.core.domain.notification.entity;

import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import com.ecommerce.shared.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Map;

public class Notification {
    private Long id;
    private Long userId;
    private NotificationType type;
    private String subject;
    private String message;
    private String recipient; // email, phone, device token, etc.
    private NotificationStatus status;
    private Map<String, Object> metadata;
    private String templateId;
    private int retryCount;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isRead;

    public Notification(Long userId, NotificationType type, String subject,
                       String message, String recipient) {
        validateSubject(subject);
        validateMessage(message);
        validateRecipient(recipient);

        this.userId = userId;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.recipient = recipient;
        this.status = NotificationStatus.PENDING;
        this.retryCount = 0;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
        this.status = NotificationStatus.READ;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementRetryCount() {
        this.retryCount++;
        this.updatedAt = LocalDateTime.now();
    }

    public void schedule(LocalDateTime scheduledTime) {
        this.scheduledAt = scheduledTime;
        this.status = NotificationStatus.SCHEDULED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean canRetry() {
        return retryCount < 3 && status == NotificationStatus.FAILED;
    }

    public boolean isScheduled() {
        return scheduledAt != null && LocalDateTime.now().isBefore(scheduledAt);
    }

    private void validateSubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            throw new ValidationException("Notification subject cannot be null or empty");
        }
        if (subject.length() > 255) {
            throw new ValidationException("Notification subject too long. Maximum length is 255 characters");
        }
    }

    private void validateMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new ValidationException("Notification message cannot be null or empty");
        }
        if (message.length() > 5000) {
            throw new ValidationException("Notification message too long. Maximum length is 5000 characters");
        }
    }

    private void validateRecipient(String recipient) {
        if (recipient == null || recipient.trim().isEmpty()) {
            throw new ValidationException("Notification recipient cannot be null or empty");
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public String getTemplateId() { return templateId; }
    public void setTemplateId(String templateId) { this.templateId = templateId; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) {
        isRead = read;
        if (read && readAt == null) {
            readAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }


}
