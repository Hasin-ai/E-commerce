package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.order.entity.Order;

public interface NotificationService {
    Long sendNotification(Notification notification);
    void sendOrderConfirmationNotification(Order order);
    void sendPaymentFailedNotification(Order order);
}