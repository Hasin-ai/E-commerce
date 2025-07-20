package com.ecommerce.core.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "gateway", length = 50)
    private String gateway;

    @Column(name = "external_transaction_id")
    private String externalTransactionId;

    @Column(name = "gateway_response")
    private String gatewayResponse;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public enum TransactionStatus {
        PENDING, SUCCESSFUL, FAILED, CANCELLED, REFUNDED
    }

    public Transaction(Long paymentId, BigDecimal amount, String currency, String paymentMethod, String gateway) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.gateway = gateway;
        this.status = TransactionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsSuccessful(String externalTransactionId, String gatewayResponse, String errorMessage, String additionalInfo) {
        this.status = TransactionStatus.SUCCESSFUL;
        this.externalTransactionId = externalTransactionId;
        this.gatewayResponse = gatewayResponse;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed(String gatewayResponse, String errorMessage) {
        this.status = TransactionStatus.FAILED;
        this.gatewayResponse = gatewayResponse;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsRefunded(String externalTransactionId, String gatewayResponse) {
        this.status = TransactionStatus.REFUNDED;
        this.externalTransactionId = externalTransactionId;
        this.gatewayResponse = gatewayResponse;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
