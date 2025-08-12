package com.ecommerce.infrastructure.external.payment;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripePaymentGatewayService implements PaymentGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(StripePaymentGatewayService.class);

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public PaymentGatewayResponse processPayment(PaymentGatewayRequest request) {
        logger.info("Creating PaymentIntent through Stripe for payment ID: {}", request.getPaymentId());

        try {
            // Ensure amount is properly rounded to 2 decimal places before converting to cents
            BigDecimal roundedAmount = request.getAmount().setScale(2, java.math.RoundingMode.HALF_UP);

            // Create PaymentIntent without auto-confirmation for webhook handling
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(roundedAmount.multiply(new BigDecimal("100")).longValue()) // Convert to cents
                .setCurrency(request.getCurrency().toLowerCase())
                .setPaymentMethod(request.getPaymentMethodId())
                .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                .putMetadata("internal_payment_id", request.getPaymentId().toString())
                .putMetadata("order_id", request.getOrderId().toString())
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            logger.info("PaymentIntent created successfully. ID: {}, Status: {}",
                       paymentIntent.getId(), paymentIntent.getStatus());

            // Return success with the PaymentIntent details for client-side confirmation
            return PaymentGatewayResponse.success(paymentIntent.getId(), paymentIntent.toJson());

        } catch (StripeException e) {
            logger.error("Stripe error creating PaymentIntent: {}", e.getMessage(), e);
            return PaymentGatewayResponse.failure("Stripe error: " + e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error creating PaymentIntent through Stripe", e);
            return PaymentGatewayResponse.failure("Gateway error: " + e.getMessage(), null);
        }
    }

    @Override
    public PaymentGatewayResponse refundPayment(String transactionId, BigDecimal amount) {
        logger.info("Processing refund through Stripe for transaction ID: {}", transactionId);

        try {
            // Ensure amount is properly rounded to 2 decimal places before converting to cents
            BigDecimal roundedAmount = amount.setScale(2, java.math.RoundingMode.HALF_UP);

            RefundCreateParams params = RefundCreateParams.builder()
                .setPaymentIntent(transactionId)
                .setAmount(roundedAmount.multiply(new BigDecimal("100")).longValue()) // Convert to cents
                .build();

            Refund refund = Refund.create(params);

            if ("succeeded".equals(refund.getStatus())) {
                logger.info("Refund processed successfully. Refund ID: {}", refund.getId());
                return PaymentGatewayResponse.success(refund.getId(), refund.toJson());
            } else {
                logger.warn("Refund failed with status: {}", refund.getStatus());
                return PaymentGatewayResponse.failure("Refund failed: " + refund.getStatus(), refund.toJson());
            }

        } catch (StripeException e) {
            logger.error("Stripe error processing refund: {}", e.getMessage(), e);
            return PaymentGatewayResponse.failure("Stripe refund error: " + e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error processing refund through Stripe", e);
            return PaymentGatewayResponse.failure("Refund failed: " + e.getMessage(), null);
        }
    }

    @Override
    public PaymentGatewayResponse getPaymentStatus(String transactionId) {
        logger.info("Getting payment status from Stripe for transaction ID: {}", transactionId);

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(transactionId);
            return PaymentGatewayResponse.success(paymentIntent.getId(), paymentIntent.toJson());

        } catch (StripeException e) {
            logger.error("Stripe error getting payment status: {}", e.getMessage(), e);
            return PaymentGatewayResponse.failure("Stripe status check error: " + e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error getting payment status from Stripe", e);
            return PaymentGatewayResponse.failure("Status check failed: " + e.getMessage(), null);
        }
    }

    /**
     * Confirm a PaymentIntent (typically called after client-side 3D Secure authentication)
     */
    public PaymentGatewayResponse confirmPayment(String paymentIntentId) {
        logger.info("Confirming PaymentIntent: {}", paymentIntentId);

        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntent confirmedPaymentIntent = paymentIntent.confirm();

            logger.info("PaymentIntent confirmed. Status: {}", confirmedPaymentIntent.getStatus());
            return PaymentGatewayResponse.success(confirmedPaymentIntent.getId(), confirmedPaymentIntent.toJson());

        } catch (StripeException e) {
            logger.error("Stripe error confirming PaymentIntent: {}", e.getMessage(), e);
            return PaymentGatewayResponse.failure("Stripe confirmation error: " + e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error confirming PaymentIntent", e);
            return PaymentGatewayResponse.failure("Confirmation failed: " + e.getMessage(), null);
        }
    }

    /**
     * Create a Stripe Checkout Session for hosted checkout experience
     */
    public PaymentGatewayResponse createCheckoutSession(PaymentGatewayRequest request) {
        logger.info("Creating Stripe Checkout Session for payment ID: {}", request.getPaymentId());

        try {
            // Ensure amount is properly rounded to 2 decimal places before converting to cents
            BigDecimal roundedAmount = request.getAmount().setScale(2, java.math.RoundingMode.HALF_UP);

            // Build line items for the checkout session
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName("Order #" + request.getOrderId())
                    .setDescription("E-commerce order payment")
                    .build();

            SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency(request.getCurrency().toLowerCase())
                    .setUnitAmount(roundedAmount.multiply(new BigDecimal("100")).longValue()) // Convert to cents
                    .setProductData(productData)
                    .build();

            SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(priceData)
                    .build();

            // Set up metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("internal_payment_id", request.getPaymentId().toString());
            metadata.put("order_id", request.getOrderId().toString());

            // Create checkout session
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/checkout/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseUrl + "/checkout/cancel")
                .addLineItem(lineItem)
                .putAllMetadata(metadata)
                .setClientReferenceId(request.getPaymentId().toString())
                .build();

            Session session = Session.create(params);

            logger.info("Checkout Session created successfully. ID: {}, URL: {}",
                       session.getId(), session.getUrl());

            return PaymentGatewayResponse.success(session.getId(), session.toJson());

        } catch (StripeException e) {
            logger.error("Stripe error creating Checkout Session: {}", e.getMessage(), e);
            return PaymentGatewayResponse.failure("Stripe error: " + e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error creating Checkout Session", e);
            return PaymentGatewayResponse.failure("Gateway error: " + e.getMessage(), null);
        }
    }

    /**
     * Retrieve a Stripe Checkout Session
     */
    public PaymentGatewayResponse getCheckoutSession(String sessionId) {
        logger.info("Retrieving Stripe Checkout Session: {}", sessionId);

        try {
            Session session = Session.retrieve(sessionId);
            return PaymentGatewayResponse.success(session.getId(), session.toJson());

        } catch (StripeException e) {
            logger.error("Stripe error retrieving Checkout Session: {}", e.getMessage(), e);
            return PaymentGatewayResponse.failure("Stripe error: " + e.getMessage(), null);
        } catch (Exception e) {
            logger.error("Error retrieving Checkout Session", e);
            return PaymentGatewayResponse.failure("Gateway error: " + e.getMessage(), null);
        }
    }
}
