package com.ecommerce.infrastructure.external.notification;

import com.ecommerce.core.usecase.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SMSService {

    private final RestTemplate restTemplate;

    @Value("${app.sms.provider.url:}")
    private String smsProviderUrl;

    @Value("${app.sms.provider.api-key:}")
    private String apiKey;

    @Value("${app.sms.enabled:false}")
    private boolean smsEnabled;

    @Value("${app.sms.from-number:+1234567890}")
    private String fromNumber;

    public void sendSms(NotificationService.SmsNotificationRequest request) {
        if (!smsEnabled) {
            log.info("SMS sending is disabled. Skipping SMS to: {}", request.getPhoneNumber());
            return;
        }

        if (smsProviderUrl == null || smsProviderUrl.isEmpty()) {
            log.warn("SMS provider URL not configured. Using mock SMS service.");
            sendMockSms(request);
            return;
        }

        try {
            sendRealSms(request);
        } catch (Exception e) {
            log.error("Failed to send SMS to: {}", request.getPhoneNumber(), e);
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

    private void sendRealSms(NotificationService.SmsNotificationRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("to", request.getPhoneNumber());
        requestBody.put("from", fromNumber);
        requestBody.put("message", processTemplate(request));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.exchange(smsProviderUrl, HttpMethod.POST, entity, String.class);
            log.info("SMS sent successfully to: {}", request.getPhoneNumber());
        } catch (Exception e) {
            log.error("Failed to send SMS via provider to: {}", request.getPhoneNumber(), e);
            throw e;
        }
    }

    private void sendMockSms(NotificationService.SmsNotificationRequest request) {
        // Mock SMS service for development/testing
        log.info("MOCK SMS - To: {}, Message: {}",
                request.getPhoneNumber(), processTemplate(request));
    }

    private String processTemplate(NotificationService.SmsNotificationRequest request) {
        String message = request.getMessage();

        if (request.getTemplateVariables() != null) {
            for (Map.Entry<String, Object> entry : request.getTemplateVariables().entrySet()) {
                message = message.replace("{{" + entry.getKey() + "}}", String.valueOf(entry.getValue()));
            }
        }

        return message;
    }

    public void sendOrderConfirmationSms(String phoneNumber, String orderNumber, String customerName) {
        NotificationService.SmsNotificationRequest request = NotificationService.SmsNotificationRequest.builder()
                .phoneNumber(phoneNumber)
                .message("Hi {{customerName}}, your order #{{orderNumber}} has been confirmed. Thank you for shopping with us!")
                .templateVariables(Map.of(
                        "customerName", customerName,
                        "orderNumber", orderNumber
                ))
                .build();

        sendSms(request);
    }

    public void sendOrderShippedSms(String phoneNumber, String orderNumber, String trackingNumber) {
        NotificationService.SmsNotificationRequest request = NotificationService.SmsNotificationRequest.builder()
                .phoneNumber(phoneNumber)
                .message("Your order #{{orderNumber}} has been shipped! Track it with: {{trackingNumber}}")
                .templateVariables(Map.of(
                        "orderNumber", orderNumber,
                        "trackingNumber", trackingNumber
                ))
                .build();

        sendSms(request);
    }

    public void sendPasswordResetSms(String phoneNumber, String resetCode, String customerName) {
        NotificationService.SmsNotificationRequest request = NotificationService.SmsNotificationRequest.builder()
                .phoneNumber(phoneNumber)
                .message("Hi {{customerName}}, your password reset code is: {{resetCode}}. This code expires in 10 minutes.")
                .templateVariables(Map.of(
                        "customerName", customerName,
                        "resetCode", resetCode
                ))
                .build();

        sendSms(request);
    }

    public void sendPromotionalSms(String phoneNumber, String customerName, String promoCode, String discount) {
        NotificationService.SmsNotificationRequest request = NotificationService.SmsNotificationRequest.builder()
                .phoneNumber(phoneNumber)
                .message("Hi {{customerName}}! Enjoy {{discount}} off with code {{promoCode}}. Valid until midnight!")
                .templateVariables(Map.of(
                        "customerName", customerName,
                        "promoCode", promoCode,
                        "discount", discount
                ))
                .build();

        sendSms(request);
    }
}
