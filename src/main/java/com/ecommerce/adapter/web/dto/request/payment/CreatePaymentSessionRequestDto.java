package com.ecommerce.adapter.web.dto.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a payment session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentSessionRequestDto {
    
    /**
     * The ID of the order to create a payment session for.
     */
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    /**
     * The URL to redirect to after a successful payment.
     */
    @NotNull(message = "Success URL is required")
    private String successUrl;
    
    /**
     * The URL to redirect to after a cancelled payment.
     */
    @NotNull(message = "Cancel URL is required")
    private String cancelUrl;
}