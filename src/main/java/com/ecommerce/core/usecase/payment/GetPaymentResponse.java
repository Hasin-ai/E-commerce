package com.ecommerce.core.usecase.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GetPaymentResponse {
    private Long id;
    private String paymentId;
    private Long orderId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public GetPaymentResponse(Long id, String paymentId, Long orderId, BigDecimal amount,
                            String currency, String paymentMethod, String status, String transactionId,
                            LocalDateTime createdAt, LocalDateTime processedAt) {
        this.id = id;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionId = transactionId;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getPaymentId() { return paymentId; }
    public Long getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
}