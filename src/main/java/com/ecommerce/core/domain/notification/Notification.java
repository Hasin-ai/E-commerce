package com.ecommerce.core.domain.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_read")
    private boolean read = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    // Email/SMS specific fields
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private String errorMessage;

    public enum NotificationType {
        ORDER_CONFIRMATION,
        PAYMENT_SUCCESS,
        PAYMENT_FAILED,
        ORDER_SHIPPED,
        ORDER_DELIVERED,
        PASSWORD_RESET,
        ACCOUNT_CREATED,
        PROMOTION,
        SYSTEM_ALERT
    }

    public enum NotificationStatus {
        PENDING,
        SENT,
        FAILED,
        READ
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = NotificationStatus.PENDING;
        }
    }
}
