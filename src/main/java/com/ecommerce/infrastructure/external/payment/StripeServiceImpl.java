package com.ecommerce.infrastructure.external.payment;

import com.ecommerce.core.usecase.payment.StripeService;
import com.ecommerce.infrastructure.config.StripeConfig;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentRetrieveParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Real implementation of StripeService that connects to Stripe API.
 * This replaces the mock implementation for production use.
 */
@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    private final StripeConfig stripeConfig;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeConfig.getSecretKey();
        log.info("Stripe API initialized with secret key");
    }

    @Override
    public CreateSessionResponse createPaymentSession(CreateSessionRequest request) {
        try {
            log.info("Creating Stripe payment session for order: {}, amount: {}",
                    request.getOrderId(), request.getAmount());

            // Convert amount to cents (Stripe expects amounts in cents)
            long amountInCents = request.getAmount().multiply(BigDecimal.valueOf(100)).longValue();

            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(stripeConfig.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(stripeConfig.getCancelUrl())
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(request.getCurrency().toLowerCase())
                                    .setUnitAmount(amountInCents)
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(request.getDescription())
                                            .build())
                                    .build())
                            .setQuantity(1L)
                            .build());

            // Add customer email if provided
            if (request.getCustomerEmail() != null && !request.getCustomerEmail().isEmpty()) {
                paramsBuilder.setCustomerEmail(request.getCustomerEmail());
            }

            // Add metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("order_id", request.getOrderId().toString());
            paramsBuilder.putAllMetadata(metadata);

            SessionCreateParams params = paramsBuilder.build();
            Session session = Session.create(params);

            return CreateSessionResponse.builder()
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .paymentIntentId(session.getPaymentIntent())
                    .build();

        } catch (StripeException e) {
            log.error("Error creating Stripe payment session: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create payment session: " + e.getMessage(), e);
        }
    }

    @Override
    public VerifyPaymentResponse verifyPayment(String paymentIntentId) {
        try {
            log.info("Verifying Stripe payment with intent ID: {}", paymentIntentId);

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            boolean successful = "succeeded".equals(paymentIntent.getStatus());
            String status = paymentIntent.getStatus();
            String errorMessage = null;

            if (!successful && paymentIntent.getLastPaymentError() != null) {
                errorMessage = paymentIntent.getLastPaymentError().getMessage();
            }

            return VerifyPaymentResponse.builder()
                    .successful(successful)
                    .status(status)
                    .errorMessage(errorMessage)
                    .build();

        } catch (StripeException e) {
            log.error("Error verifying Stripe payment: {}", e.getMessage(), e);
            return VerifyPaymentResponse.builder()
                    .successful(false)
                    .status("error")
                    .errorMessage("Failed to verify payment: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public WebhookEvent processWebhook(String payload, String signature) {
        try {
            log.info("Processing Stripe webhook event");

            Event event = Webhook.constructEvent(payload, signature, stripeConfig.getWebhookSecret());

            String eventType = event.getType();
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            if (stripeObject instanceof PaymentIntent) {
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

                return WebhookEvent.builder()
                        .eventType(eventType)
                        .paymentIntentId(paymentIntent.getId())
                        .status(paymentIntent.getStatus())
                        .amount(BigDecimal.valueOf(paymentIntent.getAmount()).divide(BigDecimal.valueOf(100)))
                        .currency(paymentIntent.getCurrency())
                        .customerEmail(paymentIntent.getReceiptEmail())
                        .successful("succeeded".equals(paymentIntent.getStatus()))
                        .build();
            }

            // Handle other event types as needed
            return WebhookEvent.builder()
                    .eventType(eventType)
                    .successful(true)
                    .build();

        } catch (SignatureVerificationException e) {
            log.error("Invalid webhook signature: {}", e.getMessage());
            throw new RuntimeException("Invalid webhook signature", e);
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process webhook", e);
        }
    }

    @Override
    public RefundResponse refundPayment(String paymentIntentId, BigDecimal amount) {
        try {
            log.info("Processing Stripe refund for payment intent: {}, amount: {}", paymentIntentId, amount);

            RefundCreateParams.Builder paramsBuilder = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId);

            // If amount is specified, convert to cents
            if (amount != null) {
                long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();
                paramsBuilder.setAmount(amountInCents);
            }

            RefundCreateParams params = paramsBuilder.build();
            Refund refund = Refund.create(params);

            return RefundResponse.builder()
                    .successful("succeeded".equals(refund.getStatus()))
                    .refundId(refund.getId())
                    .amount(BigDecimal.valueOf(refund.getAmount()).divide(BigDecimal.valueOf(100)))
                    .status(refund.getStatus())
                    .build();

        } catch (StripeException e) {
            log.error("Error processing Stripe refund: {}", e.getMessage(), e);
            return RefundResponse.builder()
                    .successful(false)
                    .errorMessage("Failed to process refund: " + e.getMessage())
                    .build();
        }
    }
}
