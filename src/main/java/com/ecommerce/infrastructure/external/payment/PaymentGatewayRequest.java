package com.ecommerce.infrastructure.external.payment;

import java.math.BigDecimal;

public class PaymentGatewayRequest {
    private String paymentId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethodId;
    private String customerId;

    public PaymentGatewayRequest(String paymentId, BigDecimal amount, String currency, 
                               String paymentMethodId, String customerId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethodId = paymentMethodId;
        this.customerId = customerId;
    }

    // Getters
    public String getPaymentId() { return paymentId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public String getCustomerId() { return customerId; }
}