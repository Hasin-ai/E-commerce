package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.infrastructure.service.NotificationService;
import com.ecommerce.core.usecase.notification.SendNotificationRequest;
import com.ecommerce.core.usecase.notification.EmailNotificationRequest;
import com.ecommerce.core.usecase.notification.PushNotificationRequest;
import com.ecommerce.core.domain.notification.valueobject.NotificationStatus;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import com.ecommerce.infrastructure.external.notification.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public Long sendNotification(Notification notification) {
        log.info("Sending notification to user: {}, type: {}", notification.getUserId(), notification.getType());

        // Save the notification first
        Notification savedNotification = notificationRepository.save(notification);

        // Send the notification based on type
        try {
            sendNotificationByType(savedNotification);
            savedNotification.markAsSent();
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage(), e);
            savedNotification.markAsFailed();
        }

        // Update the notification status
        notificationRepository.save(savedNotification);

        return savedNotification.getId();
    }

    @Override
    public void sendOrderConfirmationNotification(Order order) {
        log.info("Sending order confirmation notification for order: {}", order.getId());

        Optional<User> userOpt = userRepository.findById(order.getUserId());
        if (userOpt.isEmpty()) {
            log.error("User not found for order: {}", order.getId());
            return;
        }
        User user = userOpt.get();

        Notification notification = Notification.builder()
                .userId(order.getUserId())
                .type(NotificationType.EMAIL)
                .recipient(user.getEmail().getValue())
                .subject("Order Confirmation - Order #" + order.getId())
                .message("Your order has been confirmed and is being processed. Order ID: " + order.getId())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        sendNotification(notification);
    }

    @Override
    public void sendPaymentFailedNotification(Order order) {
        log.info("Sending payment failed notification for order: {}", order.getId());

        Optional<User> userOpt = userRepository.findById(order.getUserId());
        if (userOpt.isEmpty()) {
            log.error("User not found for order: {}", order.getId());
            return;
        }
        User user = userOpt.get();

        Notification notification = Notification.builder()
                .userId(order.getUserId())
                .type(NotificationType.EMAIL)
                .recipient(user.getEmail().getValue())
                .subject("Payment Failed - Order #" + order.getId())
                .message("Payment for your order failed. Please try again or contact support. Order ID: " + order.getId())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        sendNotification(notification);
    }

    private void sendNotificationByType(Notification notification) {
        switch (notification.getType()) {
            case EMAIL:
                emailService.sendEmail(EmailNotificationRequest.builder()
                        .to(notification.getRecipient())
                        .subject(notification.getSubject())
                        .message(notification.getMessage())
                        .build());
                break;
            case SMS:
                log.info("SMS notification would be sent to: {}", notification.getRecipient());
                break;
            case PUSH:
                log.info("Push notification would be sent to: {}", notification.getRecipient());
                break;
            default:
                log.warn("Unsupported notification type: {}", notification.getType());
        }
    }
}
