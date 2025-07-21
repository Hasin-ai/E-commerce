package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GetUserNotificationsUseCase {

    private final NotificationRepository notificationRepository;

    public List<Notification> execute(Long userId) {
        log.debug("Getting all notifications for user: {}", userId);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Page<Notification> execute(Long userId, Pageable pageable) {
        log.debug("Getting notifications for user: {} with pagination", userId);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        log.debug("Getting unread notifications for user: {}", userId);
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId);
    }

    public Long getUnreadCount(Long userId) {
        log.debug("Getting unread notification count for user: {}", userId);
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        log.debug("Marking notification {} as read for user: {}", notificationId, userId);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));

        // Use field access directly since Lombok generates getters/setters
        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("Notification does not belong to user: " + userId);
        }

        if (!notification.isRead()) {
            // Update notification fields directly using setters
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            notification.setStatus(Notification.NotificationStatus.READ);
            notificationRepository.save(notification);
        }
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        log.debug("Marking all notifications as read for user: {}", userId);
        notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        log.debug("Deleting notification {} for user: {}", notificationId, userId);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));

        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("Notification does not belong to user: " + userId);
        }

        notificationRepository.delete(notification);
    }
}
