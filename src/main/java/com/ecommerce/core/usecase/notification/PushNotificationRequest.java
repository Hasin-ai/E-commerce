package com.ecommerce.core.usecase.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationRequest {
    private String deviceToken;
    private String title;
    private String message;
    private Map<String, Object> data;
    private String sound;
    private String badge;
}