package com.ecommerce.infrastructure.external.payment;

public interface PaymentGatewayService {
    PaymentGatewayResponse processPayment(PaymentGatewayRequest request);
    PaymentGatewayResponse refundPayment(String transactionId, java.math.BigDecimal amount);
    PaymentGatewayResponse getPaymentStatus(String transactionId);
}