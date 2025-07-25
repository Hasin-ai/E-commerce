package com.ecommerce.core.domain.payment.valueobject;

public enum PaymentStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED,
    REQUIRES_ACTION
}
