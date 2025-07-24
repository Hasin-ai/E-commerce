package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.entity.NotificationChannel;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationChannel channel;
    private Map<String, Object> data;
}