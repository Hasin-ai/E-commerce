package com.ecommerce.core.usecase.payment;

import java.math.BigDecimal;

public class ProcessPaymentRequest {
    private Long userId;
    private Long orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String paymentMethodId;
    private String customerId;

    public ProcessPaymentRequest(Long userId, Long orderId, BigDecimal amount, String currency,
                               String paymentMethod, String paymentMethodId, String customerId) {
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.paymentMethodId = paymentMethodId;
        this.customerId = customerId;
    }

    // Getters
    public Long getUserId() { return userId; }
    public Long getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public String getCustomerId() { return customerId; }
}