package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.domain.notification.Notification;
import com.ecommerce.core.usecase.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        notificationService.deleteNotification(notificationId, userId);
        return ResponseEntity.ok().build();
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        // Extract user ID from authentication - implementation depends on your auth system
        return 1L; // Placeholder - implement based on your authentication
    }
}
