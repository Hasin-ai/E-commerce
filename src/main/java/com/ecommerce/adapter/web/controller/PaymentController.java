package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.payment.StripeService;
import com.ecommerce.core.usecase.notification.NotificationService;
import com.ecommerce.core.domain.notification.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final StripeService stripeService;
    private final NotificationService notificationService;

    @PostMapping("/create-session")
    public ResponseEntity<Map<String, String>> createPaymentSession(@RequestBody CreatePaymentSessionRequest request) {
        try {
            log.info("Creating payment session for order: {}", request.getOrderId());

            StripeService.CreateSessionRequest stripeRequest = StripeService.CreateSessionRequest.builder()
                    .orderId(request.getOrderId())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .description(request.getDescription())
                    .customerEmail(request.getCustomerEmail())
                    .build();

            StripeService.CreateSessionResponse response = stripeService.createPaymentSession(stripeRequest);

            Map<String, String> result = new HashMap<>();
            result.put("sessionId", response.getSessionId());
            result.put("sessionUrl", response.getSessionUrl());
            result.put("paymentIntentId", response.getPaymentIntentId());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Error creating payment session: {}", e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create payment session: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/verify/{paymentIntentId}")
    public ResponseEntity<Map<String, Object>> verifyPayment(@PathVariable String paymentIntentId) {
        try {
            log.info("Verifying payment: {}", paymentIntentId);

            StripeService.VerifyPaymentResponse response = stripeService.verifyPayment(paymentIntentId);

            Map<String, Object> result = new HashMap<>();
            result.put("successful", response.isSuccessful());
            result.put("status", response.getStatus());
            result.put("errorMessage", response.getErrorMessage());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Error verifying payment: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("successful", false);
            error.put("error", "Failed to verify payment: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/refund/{paymentIntentId}")
    public ResponseEntity<Map<String, Object>> refundPayment(
            @PathVariable String paymentIntentId,
            @RequestParam(required = false) BigDecimal amount) {
        try {
            log.info("Processing refund for payment: {}, amount: {}", paymentIntentId, amount);

            StripeService.RefundResponse response = stripeService.refundPayment(paymentIntentId, amount);

            Map<String, Object> result = new HashMap<>();
            result.put("successful", response.isSuccessful());
            result.put("refundId", response.getRefundId());
            result.put("amount", response.getAmount());
            result.put("status", response.getStatus());
            result.put("errorMessage", response.getErrorMessage());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Error processing refund: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("successful", false);
            error.put("error", "Failed to process refund: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            HttpServletRequest request,
            @RequestHeader("Stripe-Signature") String signature) {
        try {
            String payload = getRequestBody(request);
            log.info("Received Stripe webhook");

            StripeService.WebhookEvent event = stripeService.processWebhook(payload, signature);

            // Process the webhook event
            processWebhookEvent(event);

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Webhook processing failed: " + e.getMessage());
        }
    }

    private void processWebhookEvent(StripeService.WebhookEvent event) {
        try {
            switch (event.getEventType()) {
                case "payment_intent.succeeded":
                    handlePaymentSuccess(event);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentFailed(event);
                    break;
                default:
                    log.info("Unhandled webhook event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing webhook event: {}", e.getMessage(), e);
        }
    }

    private void handlePaymentSuccess(StripeService.WebhookEvent event) {
        log.info("Payment succeeded: {}", event.getPaymentIntentId());

        // Send success notification
        if (event.getCustomerEmail() != null) {
            notificationService.sendNotification(NotificationService.SendNotificationRequest.builder()
                    .userId(1L) // Get actual user ID from payment/order
                    .type(Notification.NotificationType.PAYMENT_SUCCESS)
                    .title("Payment Successful")
                    .message("Your payment of " + event.getAmount() + " " + event.getCurrency() + " has been processed successfully.")
                    .email(event.getCustomerEmail())
                    .build());
        }
    }

    private void handlePaymentFailed(StripeService.WebhookEvent event) {
        log.info("Payment failed: {}", event.getPaymentIntentId());

        // Send failure notification
        if (event.getCustomerEmail() != null) {
            notificationService.sendNotification(NotificationService.SendNotificationRequest.builder()
                    .userId(1L) // Get actual user ID from payment/order
                    .type(Notification.NotificationType.PAYMENT_FAILED)
                    .title("Payment Failed")
                    .message("Your payment could not be processed. Please try again or contact support.")
                    .email(event.getCustomerEmail())
                    .build());
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        request.getReader().lines().forEach(sb::append);
        return sb.toString();
    }

    // DTO classes
    public static class CreatePaymentSessionRequest {
        public Long orderId;
        public BigDecimal amount;
        public String currency = "usd";
        public String description;
        public String customerEmail;
        
        // Getters and setters
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    }
}
