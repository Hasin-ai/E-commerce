package com.ecommerce.infrastructure.external.payment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class StripePaymentGatewayService implements PaymentGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(StripePaymentGatewayService.class);

    @Value("${stripe.secret-key:sk_test_dummy}")
    private String stripeSecretKey;

    @Override
    public PaymentGatewayResponse processPayment(PaymentGatewayRequest request) {
        logger.info("Processing payment through Stripe for payment ID: {}", request.getPaymentId());
        
        try {
            // Simulate Stripe API call
            // In real implementation, you would use Stripe SDK here
            
            // For demo purposes, simulate success for amounts < 1000, failure otherwise
            if (request.getAmount().compareTo(new BigDecimal("1000")) < 0) {
                String transactionId = "txn_" + UUID.randomUUID().toString().substring(0, 16);
                String rawResponse = String.format(
                    "{\"id\":\"%s\",\"status\":\"succeeded\",\"amount\":%d,\"currency\":\"%s\"}",
                    transactionId,
                    request.getAmount().multiply(new BigDecimal("100")).intValue(), // Stripe uses cents
                    request.getCurrency().toLowerCase()
                );
                
                logger.info("Payment processed successfully. Transaction ID: {}", transactionId);
                return PaymentGatewayResponse.success(transactionId, rawResponse);
            } else {
                String rawResponse = "{\"error\":{\"message\":\"Your card was declined.\"}}";
                logger.warn("Payment failed for payment ID: {}", request.getPaymentId());
                return PaymentGatewayResponse.failure("Payment declined", rawResponse);
            }
            
        } catch (Exception e) {
            logger.error("Error processing payment through Stripe", e);
            return PaymentGatewayResponse.failure("Gateway error: " + e.getMessage(), null);
        }
    }

    @Override
    public PaymentGatewayResponse refundPayment(String transactionId, BigDecimal amount) {
        logger.info("Processing refund through Stripe for transaction ID: {}", transactionId);
        
        try {
            // Simulate Stripe refund API call
            String refundId = "re_" + UUID.randomUUID().toString().substring(0, 16);
            String rawResponse = String.format(
                "{\"id\":\"%s\",\"status\":\"succeeded\",\"amount\":%d}",
                refundId,
                amount.multiply(new BigDecimal("100")).intValue()
            );
            
            logger.info("Refund processed successfully. Refund ID: {}", refundId);
            return PaymentGatewayResponse.success(refundId, rawResponse);
            
        } catch (Exception e) {
            logger.error("Error processing refund through Stripe", e);
            return PaymentGatewayResponse.failure("Refund failed: " + e.getMessage(), null);
        }
    }

    @Override
    public PaymentGatewayResponse getPaymentStatus(String transactionId) {
        logger.info("Getting payment status from Stripe for transaction ID: {}", transactionId);
        
        try {
            // Simulate Stripe retrieve payment API call
            String rawResponse = String.format(
                "{\"id\":\"%s\",\"status\":\"succeeded\"}",
                transactionId
            );
            
            return PaymentGatewayResponse.success(transactionId, rawResponse);
            
        } catch (Exception e) {
            logger.error("Error getting payment status from Stripe", e);
            return PaymentGatewayResponse.failure("Status check failed: " + e.getMessage(), null);
        }
    }
}