package com.ecommerce.core.domain.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund {
    private Long id;
    private Long paymentId;
    private BigDecimal amount;
    private String reason;
    private String transactionId;
    private String status;
    private LocalDateTime createdAt;

    public Refund(Long paymentId, BigDecimal amount, String reason, String transactionId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.reason = reason;
        this.transactionId = transactionId;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
}
