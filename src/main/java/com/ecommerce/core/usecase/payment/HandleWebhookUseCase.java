package com.ecommerce.core.usecase.payment;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.entity.Transaction;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.payment.repository.TransactionRepository;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for handling webhook events from Stripe.
 * This use case is responsible for processing webhook events from Stripe and updating the payment status accordingly.
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "ecommerce.webhook.enabled", havingValue = "true", matchIfMissing = false)
public class HandleWebhookUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final StripeService stripeService;

    /**
     * Handles a webhook event from Stripe.
     *
     * @param request The request containing the webhook payload and signature
     * @return The response containing the result of the webhook handling
     */
    @Transactional
    public HandleWebhookResponse execute(HandleWebhookRequest request) {
        validateRequest(request);

        // Process the webhook event
        StripeService.WebhookEvent webhookEvent = stripeService.processWebhook(request.getPayload(), request.getSignature());

        // Find the payment by payment intent ID
        Payment payment = paymentRepository.findByPaymentIntentId(webhookEvent.getPaymentIntentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with payment intent ID: " + webhookEvent.getPaymentIntentId()));

        // Handle the webhook event based on the event type
        switch (webhookEvent.getEventType()) {
            case "payment_intent.succeeded":
                handlePaymentSucceeded(payment, webhookEvent);
                break;
            case "payment_intent.payment_failed":
                handlePaymentFailed(payment, webhookEvent);
                break;
            case "charge.refunded":
                handleRefund(payment, webhookEvent);
                break;
            default:
                // For other event types, just log the event
                return HandleWebhookResponse.builder()
                        .successful(true)
                        .paymentId(payment.getId())
                        .eventType(webhookEvent.getEventType())
                        .message("Webhook event received: " + webhookEvent.getEventType())
                        .build();
        }

        return HandleWebhookResponse.builder()
                .successful(webhookEvent.isSuccessful())
                .paymentId(payment.getId())
                .eventType(webhookEvent.getEventType())
                .message(webhookEvent.isSuccessful() ? "Webhook processed successfully" : webhookEvent.getErrorMessage())
                .build();
    }

    private void handlePaymentSucceeded(Payment payment, StripeService.WebhookEvent webhookEvent) {
        // Update payment status if not already completed
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            payment.markAsCompleted();
            paymentRepository.save(payment);

            // Create a transaction record
            Transaction transaction = new Transaction(
                    payment.getId(),
                    payment.getAmount().getAmount(),
                    payment.getCurrency(),
                    "stripe",
                    "stripe"
            );
            transaction.markAsSuccessful(
                    webhookEvent.getPaymentIntentId(),
                    webhookEvent.getStatus(),
                    null,
                    null
            );
            transactionRepository.save(transaction);

            // Update the order status
            Order order = orderRepository.findById(payment.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + payment.getOrderId()));
            if (order.getStatus().toString().equals("PENDING")) {
                order.confirm();
                orderRepository.save(order);
            }
        }
    }

    private void handlePaymentFailed(Payment payment, StripeService.WebhookEvent webhookEvent) {
        // Update payment status if not already failed
        if (payment.getStatus() != PaymentStatus.FAILED) {
            payment.markAsFailed(webhookEvent.getErrorMessage());
            paymentRepository.save(payment);

            // Create a transaction record
            Transaction transaction = new Transaction(
                    payment.getId(),
                    payment.getAmount().getAmount(),
                    payment.getCurrency(),
                    "stripe",
                    "stripe"
            );
            transaction.markAsFailed("payment_failed", webhookEvent.getErrorMessage());
            transactionRepository.save(transaction);
        }
    }

    private void handleRefund(Payment payment, StripeService.WebhookEvent webhookEvent) {
        // Update payment status if not already refunded
        if (payment.getStatus() != PaymentStatus.REFUNDED) {
            payment.refund();
            paymentRepository.save(payment);

            // Create a transaction record
            Transaction transaction = new Transaction(
                    payment.getId(),
                    payment.getAmount().getAmount(),
                    payment.getCurrency(),
                    "stripe",
                    "stripe"
            );
            transaction.markAsRefunded(webhookEvent.getPaymentIntentId(), webhookEvent.getStatus());
            transactionRepository.save(transaction);

            // Update the order status if needed
            Order order = orderRepository.findById(payment.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + payment.getOrderId()));
            if (!order.getStatus().toString().equals("CANCELLED")) {
                order.cancel();
                orderRepository.save(order);
            }
        }
    }

    private void validateRequest(HandleWebhookRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getPayload() == null || request.getPayload().isEmpty()) {
            throw new BusinessException("Webhook payload is required");
        }
        if (request.getSignature() == null || request.getSignature().isEmpty()) {
            throw new BusinessException("Webhook signature is required");
        }
    }

    /**
     * Request object for handling a webhook event.
     */
    @Data
    @Builder
    public static class HandleWebhookRequest {
        private String payload;
        private String signature;
    }

    /**
     * Response object for handling a webhook event.
     */
    @Data
    @Builder
    public static class HandleWebhookResponse {
        private boolean successful;
        private Long paymentId;
        private String eventType;
        private String message;
    }
}