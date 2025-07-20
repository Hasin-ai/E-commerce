package com.ecommerce.core.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.read = false")
    Long countUnreadByUserId(@Param("userId") Long userId);

    List<Notification> findByStatusAndCreatedAtBefore(Notification.NotificationStatus status, LocalDateTime dateTime);

    List<Notification> findByTypeAndUserId(Notification.NotificationType type, Long userId);
}
