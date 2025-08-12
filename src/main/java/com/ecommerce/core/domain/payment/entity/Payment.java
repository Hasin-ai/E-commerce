package com.ecommerce.core.domain.payment.entity;

import com.ecommerce.core.domain.payment.valueobject.PaymentMethod;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private String paymentId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionId;
    private String gatewayResponse;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public Payment(String paymentId, Long orderId, Long userId, BigDecimal amount,
                   String currency, PaymentMethod method) {
        validatePaymentId(paymentId);
        validateAmount(amount);
        validateCurrency(currency);

        this.paymentId = paymentId;
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.currency = currency.toUpperCase();
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsProcessing() {
        if (this.status != PaymentStatus.PENDING) {
            throw new ValidationException("Can only process pending payments");
        }
        this.status = PaymentStatus.PROCESSING;
    }

    public void markAsCompleted(String transactionId, String gatewayResponse) {
        if (this.status != PaymentStatus.PROCESSING) {
            throw new ValidationException("Can only complete processing payments");
        }
        this.status = PaymentStatus.COMPLETED;
        this.transactionId = transactionId;
        this.gatewayResponse = gatewayResponse;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsFailed(String gatewayResponse) {
        this.status = PaymentStatus.FAILED;
        this.gatewayResponse = gatewayResponse;
        this.processedAt = LocalDateTime.now();
    }

    public void markAsRefunded() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new ValidationException("Can only refund completed payments");
        }
        this.status = PaymentStatus.REFUNDED;
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    private void validatePaymentId(String paymentId) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new ValidationException("Payment ID cannot be empty");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Payment amount must be positive");
        }
    }

    private void validateCurrency(String currency) {
        if (currency == null || currency.length() != 3) {
            throw new ValidationException("Currency must be 3 characters (ISO 4217)");
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getPaymentId() { return paymentId; }
    public Long getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public PaymentMethod getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
    public String getGatewayResponse() { return gatewayResponse; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }
}
