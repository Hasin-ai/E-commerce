package com.ecommerce.infrastructure.external.payment;

import com.ecommerce.core.usecase.payment.StripeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock implementation of StripeService for development and testing purposes.
 * This implementation simulates Stripe API calls without actually connecting to Stripe.
 */
@Service
@Profile("test")
@Slf4j
public class MockStripeServiceImpl implements StripeService {

    @Override
    public CreateSessionResponse createPaymentSession(CreateSessionRequest request) {
        log.info("Creating mock payment session for order: {}, amount: {}",
                request.getOrderId(), request.getAmount());

        String sessionId = "cs_test_" + UUID.randomUUID().toString().substring(0, 8);
        String paymentIntentId = "pi_test_" + UUID.randomUUID().toString().substring(0, 8);
        String sessionUrl = "https://checkout.stripe.com/pay/" + sessionId;

        return CreateSessionResponse.builder()
                .sessionId(sessionId)
                .sessionUrl(sessionUrl)
                .paymentIntentId(paymentIntentId)
                .build();
    }

    @Override
    public VerifyPaymentResponse verifyPayment(String paymentIntentId) {
        log.info("Verifying mock payment with intent ID: {}", paymentIntentId);

        // Mock successful verification for testing
        return VerifyPaymentResponse.builder()
                .successful(true)
                .status("succeeded")
                .errorMessage(null)
                .build();
    }

    @Override
    public WebhookEvent processWebhook(String payload, String signature) {
        log.info("Processing mock webhook event");

        return WebhookEvent.builder()
                .eventType("payment_intent.succeeded")
                .paymentIntentId("pi_test_" + UUID.randomUUID().toString().substring(0, 8))
                .status("succeeded")
                .build();
    }

    @Override
    public RefundResponse refundPayment(String paymentIntentId, BigDecimal amount) {
        log.info("Processing mock refund for payment intent: {}, amount: {}",
                paymentIntentId, amount);

        return RefundResponse.builder()
                .successful(true)
                .refundId("re_test_" + UUID.randomUUID().toString().substring(0, 8))
                .amount(amount)
                .status("succeeded")
                .build();
    }
}
