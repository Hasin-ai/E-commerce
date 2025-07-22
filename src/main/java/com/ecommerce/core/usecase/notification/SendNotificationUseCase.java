package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationUseCase {
    private final NotificationRepository notificationRepository;

    public SendNotificationUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification execute(Long userId, NotificationType type, String subject,
                              String message, String recipient) {

        Notification notification = new Notification(userId, type, subject, message, recipient);

        // Save notification first
        notification = notificationRepository.save(notification);

        // Simulate sending (in real implementation, this would integrate with actual services)
        try {
            simulateSending(notification);
            notification.markAsSent();
        } catch (Exception e) {
            notification.markAsFailed();
        }

        return notificationRepository.save(notification);
    }

    private void simulateSending(Notification notification) {
        // Simulate email/SMS/push notification sending
        // In real implementation, integrate with:
        // - SendGrid/AWS SES for email
        // - Twilio for SMS
        // - Firebase for push notifications
        System.out.println("Sending " + notification.getType() + " notification to " + notification.getRecipient());
    }
}
