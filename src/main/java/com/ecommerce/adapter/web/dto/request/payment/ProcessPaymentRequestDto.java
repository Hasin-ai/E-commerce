package com.ecommerce.adapter.web.dto.request.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for processing a payment after a user has completed the payment flow.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequestDto {
    
    /**
     * The payment intent ID from Stripe.
     */
    @NotBlank(message = "Payment intent ID is required")
    private String paymentIntentId;
    
    /**
     * The payment method used (e.g., "card", "bank_transfer").
     */
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    
    /**
     * The last four digits of the card used (if applicable).
     */
    private String cardLastFour;
    
    /**
     * The brand of the card used (if applicable).
     */
    private String cardBrand;
}