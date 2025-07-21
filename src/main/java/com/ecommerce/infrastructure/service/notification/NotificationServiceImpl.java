package com.ecommerce.infrastructure.service.notification;

import com.ecommerce.core.domain.notification.Notification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.usecase.notification.GetUserNotificationsUseCase;
import com.ecommerce.core.usecase.notification.NotificationService;
import com.ecommerce.core.usecase.notification.SendNotificationUseCase;
import com.ecommerce.infrastructure.external.notification.EmailService;
import com.ecommerce.infrastructure.external.notification.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("notificationService")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final GetUserNotificationsUseCase getUserNotificationsUseCase;
    private final EmailService emailService;
    private final SMSService smsService;

    @Override
    public Notification sendNotification(SendNotificationRequest request) {
        log.info("Processing notification request for user: {}", request.getUserId());
        
        SendNotificationUseCase sendNotificationUseCase = new SendNotificationUseCase(notificationRepository, this);
        return sendNotificationUseCase.execute(request);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return getUserNotificationsUseCase.execute(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return getUserNotificationsUseCase.getUnreadNotifications(userId);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        getUserNotificationsUseCase.markAsRead(notificationId, userId);
    }

    @Override
    public void markAllAsRead(Long userId) {
        getUserNotificationsUseCase.markAllAsRead(userId);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        return getUserNotificationsUseCase.getUnreadCount(userId);
    }

    @Override
    public void deleteNotification(Long notificationId, Long userId) {
        getUserNotificationsUseCase.deleteNotification(notificationId, userId);
    }

    @Override
    public void sendEmailNotification(EmailNotificationRequest request) {
        try {
            log.info("Sending email notification to: {}", request.getTo());
            emailService.sendEmail(request);
            log.info("Email notification sent successfully to: {}", request.getTo());
        } catch (Exception e) {
            log.error("Failed to send email notification to: {}", request.getTo(), e);
            throw new RuntimeException("Failed to send email notification", e);
        }
    }

    @Override
    public void sendSmsNotification(SmsNotificationRequest request) {
        try {
            log.info("Sending SMS notification to: {}", request.getPhoneNumber());
            smsService.sendSms(request);
            log.info("SMS notification sent successfully to: {}", request.getPhoneNumber());
        } catch (Exception e) {
            log.error("Failed to send SMS notification to: {}", request.getPhoneNumber(), e);
            throw new RuntimeException("Failed to send SMS notification", e);
        }
    }
}
