package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.notification.entity.Notification;

public interface NotificationService {
    Long sendNotification(Notification notification);
}