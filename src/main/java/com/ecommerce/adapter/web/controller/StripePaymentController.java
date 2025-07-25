package com.ecommerce.adapter.web.controller;

import com.ecommerce.infrastructure.external.payment.StripePaymentGatewayService;
import com.ecommerce.infrastructure.external.payment.PaymentGatewayResponse;
import com.ecommerce.infrastructure.external.payment.PaymentGatewayRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments/stripe")
public class StripePaymentController {

    private static final Logger logger = LoggerFactory.getLogger(StripePaymentController.class);

    private final StripePaymentGatewayService stripePaymentGatewayService;

    public StripePaymentController(StripePaymentGatewayService stripePaymentGatewayService) {
        this.stripePaymentGatewayService = stripePaymentGatewayService;
    }

    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<?> confirmPayment(@PathVariable String paymentIntentId) {
        logger.info("Confirming PaymentIntent: {}", paymentIntentId);

        try {
            PaymentGatewayResponse response = stripePaymentGatewayService.confirmPayment(paymentIntentId);

            if (response.isSuccessful()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Error confirming payment", e);
            return ResponseEntity.internalServerError()
                .body("Failed to confirm payment: " + e.getMessage());
        }
    }

    @PostMapping("/checkout-session")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CheckoutSessionRequest request) {
        logger.info("Creating Stripe Checkout Session for order: {}", request.getOrderId());

        try {
            // Convert request to payment gateway request
            PaymentGatewayRequest paymentRequest = new PaymentGatewayRequest(
                request.getPaymentId().toString(), // Convert Long to String
                request.getOrderId(),
                request.getAmount(),
                request.getCurrency(),
                null, // No payment method needed for checkout sessions
                null  // No customer ID needed for checkout sessions
            );

            PaymentGatewayResponse response = stripePaymentGatewayService.createCheckoutSession(paymentRequest);

            if (response.isSuccessful()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Error creating checkout session", e);
            return ResponseEntity.internalServerError()
                .body("Failed to create checkout session: " + e.getMessage());
        }
    }

    @GetMapping("/checkout-session/{sessionId}")
    public ResponseEntity<?> getCheckoutSession(@PathVariable String sessionId) {
        logger.info("Retrieving Stripe Checkout Session: {}", sessionId);

        try {
            PaymentGatewayResponse response = stripePaymentGatewayService.getCheckoutSession(sessionId);

            if (response.isSuccessful()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Error retrieving checkout session", e);
            return ResponseEntity.internalServerError()
                .body("Failed to retrieve checkout session: " + e.getMessage());
        }
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String transactionId) {
        logger.info("Getting payment status for transaction: {}", transactionId);

        try {
            PaymentGatewayResponse response = stripePaymentGatewayService.getPaymentStatus(transactionId);

            if (response.isSuccessful()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            logger.error("Error getting payment status", e);
            return ResponseEntity.internalServerError()
                .body("Failed to get payment status: " + e.getMessage());
        }
    }

    // Request DTO for checkout session creation
    public static class CheckoutSessionRequest {
        private Long paymentId;
        private Long orderId;
        private BigDecimal amount;
        private String currency;

        // Constructors
        public CheckoutSessionRequest() {}

        public CheckoutSessionRequest(Long paymentId, Long orderId, BigDecimal amount, String currency) {
            this.paymentId = paymentId;
            this.orderId = orderId;
            this.amount = amount;
            this.currency = currency;
        }

        // Getters and setters
        public Long getPaymentId() { return paymentId; }
        public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }
}
