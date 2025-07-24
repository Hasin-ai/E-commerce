package com.ecommerce.core.usecase.payment;

public class GetPaymentRequest {
    private Long paymentId;
    private Long userId;

    public GetPaymentRequest(Long paymentId, Long userId) {
        this.paymentId = paymentId;
        this.userId = userId;
    }

    // Getters
    public Long getPaymentId() { return paymentId; }
    public Long getUserId() { return userId; }
}