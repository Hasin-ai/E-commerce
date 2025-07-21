package com.ecommerce.adapter.web.dto.request.payment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for handling webhook events from Stripe.
 * This DTO is not directly used in the request body, as Stripe sends the payload as raw JSON.
 * Instead, it's used to encapsulate the webhook data after it's extracted from the request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookRequestDto {
    
    /**
     * The raw payload of the webhook event.
     */
    @NotBlank(message = "Webhook payload is required")
    private String payload;
    
    /**
     * The signature of the webhook event, used to verify its authenticity.
     */
    @NotBlank(message = "Webhook signature is required")
    private String signature;
}