package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.webhook.WebhookProcessingUseCase;
import com.ecommerce.core.usecase.payment.ProcessWebhookEventRequest;
import com.ecommerce.infrastructure.service.IdempotencyService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/webhooks/stripe")
public class StripeWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);

    private static final long EVENT_TTL_HOURS = 24;

    // Supported webhook event types
    private static final Set<String> SUPPORTED_EVENT_TYPES = Set.of(
            "payment_intent.succeeded",
            "payment_intent.payment_failed",
            "payment_intent.requires_action",
            "payment_intent.canceled",
            "payment_intent.processing",
            "payment_intent.amount_capturable_updated"
    );

    private final WebhookProcessingUseCase webhookProcessingUseCase;
    private final IdempotencyService idempotencyService;
    private final String webhookSecret;

    public StripeWebhookController(
            WebhookProcessingUseCase webhookProcessingUseCase,
            IdempotencyService idempotencyService,
            @Value("${stripe.webhook-secret}") String webhookSecret) {
        this.webhookProcessingUseCase = webhookProcessingUseCase;
        this.idempotencyService = idempotencyService;
        this.webhookSecret = webhookSecret;
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            HttpServletRequest request,
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        logger.info("Received Stripe webhook with signature header present: {}", sigHeader != null);

        // Validate webhook secret is configured
        if (webhookSecret == null || webhookSecret.equals("whsec_dummy")) {
            logger.error("Webhook secret not properly configured");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Webhook not configured");
        }

        try {
            // Verify webhook signature - this is critical for security
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            logger.info("Webhook event verified successfully. Type: {}, ID: {}",
                    event.getType(), event.getId());

            // Check if event type is supported
            if (!SUPPORTED_EVENT_TYPES.contains(event.getType())) {
                logger.info("Ignoring unsupported event type: {}", event.getType());
                return ResponseEntity.ok("Event type not supported");
            }

            // Implement idempotency with TTL - prevent duplicate processing
            if (idempotencyService.isEventAlreadyProcessed(event.getId())) {
                logger.info("Event {} already processed, skipping", event.getId());
                return ResponseEntity.ok("Event already processed");
            }

            // Process the webhook event
            ProcessWebhookEventRequest webhookRequest = new ProcessWebhookEventRequest(
                    event.getId(),
                    event.getType(),
                    payload,
                    event.getCreated()
            );

            webhookProcessingUseCase.execute(webhookRequest);

            // Mark event as processed
            idempotencyService.markEventAsProcessed(event.getId());

            logger.info("Webhook event processed successfully: {}", event.getId());
            return ResponseEntity.ok("Webhook processed successfully");

        } catch (SignatureVerificationException e) {
            logger.error("Invalid webhook signature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid signature");

        } catch (Exception e) {
            logger.error("Error processing webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Webhook processing failed");
        }
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Run once a day
    public void cleanupOldEvents() {
        logger.info("Cleaning up old processed events...");
        LocalDateTime cutoff = LocalDateTime.now().minusHours(EVENT_TTL_HOURS);
        idempotencyService.cleanupOldProcessedEvents(cutoff);
        logger.info("Finished cleaning up old processed events.");
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Webhook endpoint is healthy");
    }
}
