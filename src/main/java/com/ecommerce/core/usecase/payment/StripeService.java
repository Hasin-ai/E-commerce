package com.ecommerce.core.usecase.payment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Service interface for interacting with the Stripe API.
 * This interface defines the contract for payment operations with Stripe.
 */
public interface StripeService {

    /**
     * Creates a payment session with Stripe.
     *
     * @param request The request containing payment details
     * @return The response containing the session ID, URL, and payment intent ID
     */
    CreateSessionResponse createPaymentSession(CreateSessionRequest request);

    /**
     * Verifies a payment with Stripe.
     *
     * @param paymentIntentId The payment intent ID to verify
     * @return The response containing the verification result
     */
    VerifyPaymentResponse verifyPayment(String paymentIntentId);

    /**
     * Processes a webhook event from Stripe.
     *
     * @param payload The webhook payload
     * @param signature The webhook signature
     * @return The processed webhook event
     */
    WebhookEvent processWebhook(String payload, String signature);

    /**
     * Refunds a payment with Stripe.
     *
     * @param paymentIntentId The payment intent ID to refund
     * @param amount The amount to refund (null for full refund)
     * @return The response containing the refund result
     */
    RefundResponse refundPayment(String paymentIntentId, BigDecimal amount);

    /**
     * Request object for creating a payment session.
     */
    @Data
    @Builder
    class CreateSessionRequest {
        private Long orderId;
        private Long paymentId;
        private BigDecimal amount;
        private String currency;
        private String description;
        private String customerEmail;
        private String successUrl;
        private String cancelUrl;
    }

    /**
     * Response object for creating a payment session.
     */
    @Data
    @Builder
    class CreateSessionResponse {
        private String sessionId;
        private String sessionUrl;
        private String paymentIntentId;
    }

    /**
     * Response object for verifying a payment.
     */
    @Data
    @Builder
    class VerifyPaymentResponse {
        private boolean successful;
        private String status;
        private String errorMessage;
    }

    /**
     * Response object for refunding a payment.
     */
    @Data
    @Builder
    class RefundResponse {
        private boolean successful;
        private String refundId;
        private BigDecimal amount;
        private String status;
        private String errorMessage;
    }

    /**
     * Object representing a webhook event from Stripe.
     */
    @Data
    @Builder
    class WebhookEvent {
        private String eventType;
        private String paymentIntentId;
        private String status;
        private BigDecimal amount;
        private String currency;
        private String customerEmail;
        private boolean successful;
        private String errorMessage;
    }
}