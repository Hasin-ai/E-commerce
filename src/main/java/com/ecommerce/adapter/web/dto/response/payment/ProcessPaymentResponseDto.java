package com.ecommerce.adapter.web.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the response of processing a payment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentResponseDto {
    
    /**
     * Whether the payment was processed successfully.
     */
    private boolean successful;
    
    /**
     * The ID of the payment.
     */
    private Long paymentId;
    
    /**
     * The ID of the order associated with the payment.
     */
    private Long orderId;
    
    /**
     * The ID of the transaction.
     */
    private Long transactionId;
    
    /**
     * A message describing the result of the payment processing.
     */
    private String message;
}