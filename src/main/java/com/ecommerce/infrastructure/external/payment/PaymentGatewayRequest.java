package com.ecommerce.infrastructure.external.payment;

import java.math.BigDecimal;

public class PaymentGatewayRequest {
    private String paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethodId;
    private String customerId;

    public PaymentGatewayRequest(String paymentId, Long orderId, BigDecimal amount, String currency,
                               String paymentMethodId, String customerId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethodId = paymentMethodId;
        this.customerId = customerId;
    }

    // Getters
    public String getPaymentId() { return paymentId; }
    public Long getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public String getCustomerId() { return customerId; }
}