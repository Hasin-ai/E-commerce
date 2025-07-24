package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import com.ecommerce.infrastructure.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SendNotificationUseCase {
    
    private final NotificationService notificationService;
    
    public SendNotificationResponse execute(SendNotificationRequest request) {
        try {
            Notification notification = Notification.builder()
                    .userId(request.getUserId())
                    .title(request.getTitle())
                    .message(request.getMessage())
                    .type(request.getType())
                    .channel(request.getChannel())
                    .data(request.getData())
                    .status(NotificationStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            Long notificationId = notificationService.sendNotification(notification);
            
            return SendNotificationResponse.builder()
                    .notificationId(notificationId)
                    .success(true)
                    .message("Notification sent successfully")
                    .build();
                    
        } catch (Exception e) {
            return SendNotificationResponse.builder()
                    .success(false)
                    .message("Failed to send notification: " + e.getMessage())
                    .build();
        }
    }
}