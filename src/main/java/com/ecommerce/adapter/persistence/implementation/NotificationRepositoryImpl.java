package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    
    private final Map<Long, Notification> notifications = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(idGenerator.getAndIncrement());
        }
        notifications.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return Optional.ofNullable(notifications.get(id));
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return notifications.values().stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Notification> findByUserIdAndStatus(Long userId, NotificationStatus status) {
        return notifications.values().stream()
                .filter(notification -> notification.getUserId().equals(userId) && notification.getStatus() == status)
                .toList();
    }

    @Override
    public List<Notification> findByStatus(NotificationStatus status) {
        return notifications.values().stream()
                .filter(notification -> notification.getStatus() == status)
                .toList();
    }

    @Override
    public List<Notification> findByTypeAndStatus(NotificationType type, NotificationStatus status) {
        return notifications.values().stream()
                .filter(notification -> notification.getType() == type && notification.getStatus() == status)
                .toList();
    }

    @Override
    public List<Notification> findScheduledNotifications(LocalDateTime beforeTime) {
        return notifications.values().stream()
                .filter(notification -> notification.getScheduledAt() != null && 
                       notification.getScheduledAt().isBefore(beforeTime))
                .toList();
    }

    @Override
    public List<Notification> findFailedNotificationsForRetry() {
        return notifications.values().stream()
                .filter(notification -> notification.getStatus() == NotificationStatus.FAILED && 
                       notification.canRetry())
                .toList();
    }

    @Override
    public List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return notifications.values().stream()
                .filter(notification -> notification.getUserId().equals(userId))
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    @Override
    public Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        List<Notification> userNotifications = findByUserIdOrderByCreatedAtDesc(userId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), userNotifications.size());
        List<Notification> pageContent = userNotifications.subList(start, end);
        return new PageImpl<>(pageContent, pageable, userNotifications.size());
    }

    @Override
    public List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId) {
        return notifications.values().stream()
                .filter(notification -> notification.getUserId().equals(userId) && !notification.isRead())
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    @Override
    public Long countUnreadByUserId(Long userId) {
        return notifications.values().stream()
                .filter(notification -> notification.getUserId().equals(userId) && !notification.isRead())
                .count();
    }

    @Override
    public void markAllAsRead(Long userId) {
        notifications.values().stream()
                .filter(notification -> notification.getUserId().equals(userId) && !notification.isRead())
                .forEach(Notification::markAsRead);
    }

    @Override
    public void delete(Notification notification) {
        notifications.remove(notification.getId());
    }

    @Override
    public List<Notification> findByStatusAndCreatedAtBefore(NotificationStatus status, LocalDateTime dateTime) {
        return notifications.values().stream()
                .filter(notification -> notification.getStatus() == status && 
                       notification.getCreatedAt().isBefore(dateTime))
                .toList();
    }

    @Override
    public List<Notification> saveAll(List<Notification> notificationList) {
        return notificationList.stream()
                .map(this::save)
                .toList();
    }
}