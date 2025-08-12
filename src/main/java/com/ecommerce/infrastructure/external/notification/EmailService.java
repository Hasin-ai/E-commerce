package com.ecommerce.infrastructure.external.notification;

import com.ecommerce.core.usecase.notification.NotificationService;
import com.ecommerce.core.usecase.notification.EmailNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service("notificationEmailService")
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from:noreply@ecommerce.com}")
    private String fromEmail;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    public void sendEmail(EmailNotificationRequest request) {
        if (!emailEnabled) {
            log.info("Email sending is disabled. Skipping email to: {}", request.getTo());
            return;
        }

        try {
            if (request.getTemplateId() != null && !request.getTemplateId().isEmpty()) {
                sendTemplatedEmail(request);
            } else {
                sendSimpleEmail(request);
            }
        } catch (Exception e) {
            log.error("Failed to send email to: {}", request.getTo(), e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private void sendSimpleEmail(EmailNotificationRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getMessage());

        mailSender.send(message);
        log.info("Simple email sent successfully to: {}", request.getTo());
    }

    private void sendTemplatedEmail(EmailNotificationRequest request) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());

        // Process template with variables
        Context context = new Context();
        if (request.getTemplateVariables() != null) {
            for (Map.Entry<String, Object> entry : request.getTemplateVariables().entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        context.setVariable("message", request.getMessage());
        context.setVariable("subject", request.getSubject());

        String htmlContent = templateEngine.process(request.getTemplateId(), context);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
        log.info("Templated email sent successfully to: {} using template: {}",
                request.getTo(), request.getTemplateId());
    }

    public void sendOrderConfirmationEmail(String to, String orderNumber, String customerName) {
        EmailNotificationRequest request = EmailNotificationRequest.builder()
                .to(to)
                .subject("Order Confirmation - #" + orderNumber)
                .templateId("order-confirmation")
                .templateVariables(Map.of(
                        "customerName", customerName,
                        "orderNumber", orderNumber
                ))
                .build();

        sendEmail(request);
    }

    public void sendPasswordResetEmail(String to, String resetToken, String customerName) {
        EmailNotificationRequest request = EmailNotificationRequest.builder()
                .to(to)
                .subject("Password Reset Request")
                .templateId("password-reset")
                .templateVariables(Map.of(
                        "customerName", customerName,
                        "resetToken", resetToken,
                        "resetLink", "https://yourapp.com/reset-password?token=" + resetToken
                ))
                .build();

        sendEmail(request);
    }

    public void sendWelcomeEmail(String to, String customerName) {
        EmailNotificationRequest request = EmailNotificationRequest.builder()
                .to(to)
                .subject("Welcome to Our E-commerce Platform!")
                .templateId("welcome")
                .templateVariables(Map.of("customerName", customerName))
                .build();

        sendEmail(request);
    }
}
