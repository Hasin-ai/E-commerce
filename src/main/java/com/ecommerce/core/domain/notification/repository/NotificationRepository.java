package com.ecommerce.core.domain.notification.repository;

import com.ecommerce.core.domain.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    Long countUnreadByUserId(Long userId);

    List<Notification> findByStatusAndCreatedAtBefore(Notification.NotificationStatus status, LocalDateTime dateTime);

    List<Notification> findByTypeAndUserId(Notification.NotificationType type, Long userId);

    List<Notification> findByUserIdAndStatus(Long userId, Notification.NotificationStatus status);

    List<Notification> findAll();

    void deleteById(Long id);

    void delete(Notification notification);

    long count();

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    // Additional method for batch operations
    List<Notification> saveAll(List<Notification> notifications);
}
