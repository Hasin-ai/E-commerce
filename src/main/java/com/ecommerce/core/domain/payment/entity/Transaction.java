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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = TransactionStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public String getTransactionId() {
        return externalTransactionId;
    }

    public enum TransactionStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REFUNDED
    }

    // Additional constructor for ProcessPaymentUseCase
    public Transaction(Long paymentId, String transactionId, Object amount, String status, String gatewayResponse) {
        this.paymentId = paymentId;
        this.externalTransactionId = transactionId;
        if (amount instanceof com.ecommerce.core.domain.product.valueobject.Price) {
            com.ecommerce.core.domain.product.valueobject.Price price = (com.ecommerce.core.domain.product.valueobject.Price) amount;
            this.amount = price.getAmount();
            this.currency = price.getCurrency();
        } else if (amount instanceof BigDecimal) {
            this.amount = (BigDecimal) amount;
            this.currency = "USD"; // default currency
        }
        this.status = TransactionStatus.valueOf(status.toUpperCase());
        this.gatewayResponse = gatewayResponse;
        this.gateway = "stripe";
        this.createdAt = LocalDateTime.now();
    }
}
