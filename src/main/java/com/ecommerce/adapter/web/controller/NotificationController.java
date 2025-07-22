package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.SendNotificationRequestDto;
import com.ecommerce.adapter.web.dto.response.NotificationResponseDto;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/notifications")
@Validated
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    // TODO: Inject use cases when implemented
    // private final SendNotificationUseCase sendNotificationUseCase;
    // private final GetUserNotificationsUseCase getUserNotificationsUseCase;
    // private final MarkNotificationReadUseCase markNotificationReadUseCase;
    // private final DeleteNotificationUseCase deleteNotificationUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponseDto>>> getUserNotifications(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(required = false) Boolean unreadOnly) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Notifications retrieved successfully"));
    }

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Integer>> getUnreadNotificationCount(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(0, "Unread notification count retrieved successfully"));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponseDto>> markNotificationAsRead(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Notification marked as read"));
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllNotificationsAsRead(
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "All notifications marked as read"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Notification deleted successfully"));
    }

    // Admin endpoints
    @PostMapping("/admin/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendNotification(
            @Valid @RequestBody SendNotificationRequestDto requestDto) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Notification sent successfully"));
    }

    @PostMapping("/admin/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> broadcastNotification(
            @Valid @RequestBody SendNotificationRequestDto requestDto) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Broadcast notification sent successfully"));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<NotificationResponseDto>>> getAllNotifications(
            Pageable pageable,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "All notifications retrieved successfully"));
    }
}