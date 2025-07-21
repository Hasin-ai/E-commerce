package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.domain.notification.Notification;
import com.ecommerce.core.usecase.notification.NotificationService;
import com.ecommerce.shared.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtils jwtUtils;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Notification>> getUserNotifications(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Notification>> getUnreadNotifications(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Long>> getUnreadCount(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        Long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long notificationId,
            HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> markAllAsRead(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId,
            HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        notificationService.deleteNotification(notificationId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Notification> sendNotification(@RequestBody SendNotificationRequest request) {
        NotificationService.SendNotificationRequest serviceRequest = NotificationService.SendNotificationRequest.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .templateVariables(request.getTemplateVariables())
                .build();

        Notification notification = notificationService.sendNotification(serviceRequest);
        return ResponseEntity.ok(notification);
    }

    // DTO for sending notifications
    public static class SendNotificationRequest {
        private Long userId;
        private Notification.NotificationType type;
        private String title;
        private String message;
        private String email;
        private String phoneNumber;
        private Map<String, String> templateVariables;

        public SendNotificationRequest() {}

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Notification.NotificationType getType() {
            return type;
        }

        public void setType(Notification.NotificationType type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Map<String, String> getTemplateVariables() {
            return templateVariables;
        }

        public void setTemplateVariables(Map<String, String> templateVariables) {
            this.templateVariables = templateVariables;
        }
    }
}
