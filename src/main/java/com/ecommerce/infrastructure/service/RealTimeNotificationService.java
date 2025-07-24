package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RealTimeNotificationService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    public void sendToUser(Long userId, Notification notification) {
        Map<String, Object> message = new HashMap<>();
        message.put("id", notification.getId());
        message.put("title", notification.getTitle());
        message.put("message", notification.getMessage());
        message.put("type", notification.getType());
        message.put("data", notification.getData());
        message.put("timestamp", notification.getCreatedAt());
        
        messagingTemplate.convertAndSendToUser(
                userId.toString(), 
                "/queue/notifications", 
                message
        );
    }
    
    public void sendToAll(Notification notification) {
        Map<String, Object> message = new HashMap<>();
        message.put("id", notification.getId());
        message.put("title", notification.getTitle());
        message.put("message", notification.getMessage());
        message.put("type", notification.getType());
        message.put("data", notification.getData());
        message.put("timestamp", notification.getCreatedAt());
        
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}