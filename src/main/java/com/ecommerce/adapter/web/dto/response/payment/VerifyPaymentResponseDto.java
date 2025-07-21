package com.ecommerce.adapter.web.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for the response of verifying a payment status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPaymentResponseDto {
    
    /**
     * The ID of the payment.
     */
    private Long paymentId;
    
    /**
     * The ID of the order associated with the payment.
     */
    private Long orderId;
    
    /**
     * The status of the payment (e.g., "PENDING", "PROCESSING", "COMPLETED", "FAILED").
     */
    private String status;
    
    /**
     * The amount of the payment.
     */
    private BigDecimal amount;
    
    /**
     * The currency of the payment.
     */
    private String currency;
    
    /**
     * The payment intent ID from Stripe.
     */
    private String paymentIntentId;
    
    /**
     * Whether the payment was successful.
     */
    private boolean successful;
    
    /**
     * A message describing the status of the payment.
     */
    private String message;
}