package com.ecommerce.adapter.web.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for verifying a payment status.
 * Either paymentId or paymentIntentId must be provided.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPaymentRequestDto {
    
    /**
     * The ID of the payment to verify.
     */
    private Long paymentId;
    
    /**
     * The payment intent ID from Stripe.
     */
    private String paymentIntentId;
}