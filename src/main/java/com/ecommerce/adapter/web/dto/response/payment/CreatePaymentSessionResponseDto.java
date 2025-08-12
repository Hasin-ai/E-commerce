package com.ecommerce.adapter.web.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the response of creating a payment session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentSessionResponseDto {
    
    /**
     * The ID of the payment.
     */
    private Long paymentId;
    
    /**
     * The ID of the payment session.
     */
    private String sessionId;
    
    /**
     * The URL to redirect the user to for completing the payment.
     */
    private String sessionUrl;
}