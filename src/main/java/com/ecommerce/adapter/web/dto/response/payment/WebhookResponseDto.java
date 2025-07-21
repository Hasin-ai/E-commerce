package com.ecommerce.adapter.web.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the response of handling a webhook event from Stripe.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookResponseDto {
    
    /**
     * Whether the webhook event was processed successfully.
     */
    private boolean successful;
    
    /**
     * The ID of the payment associated with the webhook event.
     */
    private Long paymentId;
    
    /**
     * The type of the webhook event (e.g., "payment_intent.succeeded", "payment_intent.payment_failed").
     */
    private String eventType;
    
    /**
     * A message describing the result of processing the webhook event.
     */
    private String message;
}