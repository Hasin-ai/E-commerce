package com.ecommerce.core.domain.notification.repository;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
    List<Notification> findByUserId(Long userId);
    List<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status);
    List<Notification> findByStatus(NotificationStatus status);
    List<Notification> findByTypeAndStatus(NotificationType type, NotificationStatus status);
    List<Notification> findScheduledNotifications(LocalDateTime beforeTime);
    List<Notification> findFailedNotificationsForRetry();

    // Missing methods needed by use cases
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);
    Long countUnreadByUserId(Long userId);
    void markAllAsRead(Long userId);
    void delete(Notification notification);
    List<Notification> findByStatusAndCreatedAtBefore(NotificationStatus status, LocalDateTime dateTime);
    List<Notification> saveAll(List<Notification> notifications);
}
