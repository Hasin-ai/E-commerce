package com.ecommerce.core.domain.payment.valueobject;

/**
 * Represents the status of a payment in the system.
 */
public enum PaymentStatus {
    /**
     * Payment has been initiated but not processed yet
     */
    PENDING,
    
    /**
     * Payment is being processed by the payment gateway
     */
    PROCESSING,
    
    /**
     * Payment has been successfully processed
     */
    COMPLETED,
    
    /**
     * Payment has failed due to an error
     */
    FAILED,
    
    /**
     * Payment has been refunded
     */
    REFUNDED,
    
    /**
     * Payment has been cancelled before processing
     */
    CANCELLED
}