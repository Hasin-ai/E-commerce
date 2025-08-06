package com.ecommerce.core.usecase.payment;

import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.usecase.webhook.WebhookProcessingUseCase;
import com.ecommerce.infrastructure.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class ProcessWebhookEventUseCase implements WebhookProcessingUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ProcessWebhookEventUseCase.class);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public ProcessWebhookEventUseCase(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void execute(ProcessWebhookEventRequest request) {
        logger.info("Processing webhook event: {} of type: {}",
                request.getEventId(), request.getEventType());

        try {
            switch (request.getEventType()) {
                case "payment_intent.succeeded":
                    handlePaymentSucceeded(request);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentFailed(request);
                    break;
                case "payment_intent.requires_action":
                    handlePaymentRequiresAction(request);
                    break;
                case "payment_intent.canceled":
                    handlePaymentCanceled(request);
                    break;
                case "payment_intent.processing":
                    handlePaymentProcessing(request);
                    break;
                default:
                    logger.info("Unhandled webhook event type: {}", request.getEventType());
            }
        } catch (Exception e) {
            logger.error("Error processing webhook event {}: {}",
                    request.getEventId(), e.getMessage(), e);
            throw new RuntimeException("Failed to process webhook event", e);
        }
    }

    private void handlePaymentSucceeded(ProcessWebhookEventRequest request) {
        logger.info("Handling payment succeeded event: {}", request.getEventId());

        try {
            JsonNode eventData = objectMapper.readTree(request.getPayload());
            JsonNode paymentIntentData = eventData.get("data").get("object");

            String paymentIntentId = paymentIntentData.get("id").asText();
            JsonNode metadata = paymentIntentData.get("metadata");

            if (metadata != null && metadata.has("internal_payment_id")) {
                Long internalPaymentId = Long.valueOf(metadata.get("internal_payment_id").asText());
                Long orderId = Long.valueOf(metadata.get("order_id").asText());

                // Update payment status
                Optional<Payment> paymentOpt = paymentRepository.findById(internalPaymentId);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.updateStatus(PaymentStatus.COMPLETED);
                    payment.setTransactionId(paymentIntentId);
                    payment.setProcessedAt(LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(request.getCreatedTimestamp()),
                            ZoneId.systemDefault()));
                    paymentRepository.save(payment);

                    logger.info("Payment {} marked as completed", internalPaymentId);
                }

                // Update order status
                Optional<Order> orderOpt = orderRepository.findById(orderId);
                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    order.confirm();
                    orderRepository.save(order);

                    logger.info("Order {} marked as confirmed", orderId);

                    // Send notification
                    notificationService.sendOrderConfirmationNotification(order);
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment succeeded event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to handle payment succeeded event", e);
        }
    }

    private void handlePaymentFailed(ProcessWebhookEventRequest request) {
        logger.info("Handling payment failed event: {}", request.getEventId());

        try {
            JsonNode eventData = objectMapper.readTree(request.getPayload());
            JsonNode paymentIntentData = eventData.get("data").get("object");

            JsonNode metadata = paymentIntentData.get("metadata");

            if (metadata != null && metadata.has("internal_payment_id")) {
                Long internalPaymentId = Long.valueOf(metadata.get("internal_payment_id").asText());
                Long orderId = Long.valueOf(metadata.get("order_id").asText());

                // Update payment status
                Optional<Payment> paymentOpt = paymentRepository.findById(internalPaymentId);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.updateStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment);

                    logger.info("Payment {} marked as failed", internalPaymentId);
                }

                // Update order status
                Optional<Order> orderOpt = orderRepository.findById(orderId);
                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    order.handlePaymentFailed();
                    orderRepository.save(order);

                    logger.info("Order {} marked as payment failed", orderId);

                    // Send notification
                    notificationService.sendPaymentFailedNotification(order);
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment failed event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to handle payment failed event", e);
        }
    }

    private void handlePaymentRequiresAction(ProcessWebhookEventRequest request) {
        logger.info("Handling payment requires action event: {}", request.getEventId());

        try {
            JsonNode eventData = objectMapper.readTree(request.getPayload());
            JsonNode paymentIntentData = eventData.get("data").get("object");

            JsonNode metadata = paymentIntentData.get("metadata");

            if (metadata != null && metadata.has("internal_payment_id")) {
                Long internalPaymentId = Long.valueOf(metadata.get("internal_payment_id").asText());

                // Update payment status to require action
                Optional<Payment> paymentOpt = paymentRepository.findById(internalPaymentId);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.updateStatus(PaymentStatus.REQUIRES_ACTION);
                    paymentRepository.save(payment);

                    logger.info("Payment {} marked as requires action", internalPaymentId);
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment requires action event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to handle payment requires action event", e);
        }
    }

    private void handlePaymentCanceled(ProcessWebhookEventRequest request) {
        logger.info("Handling payment canceled event: {}", request.getEventId());

        try {
            JsonNode eventData = objectMapper.readTree(request.getPayload());
            JsonNode paymentIntentData = eventData.get("data").get("object");

            JsonNode metadata = paymentIntentData.get("metadata");

            if (metadata != null && metadata.has("internal_payment_id")) {
                Long internalPaymentId = Long.valueOf(metadata.get("internal_payment_id").asText());
                Long orderId = Long.valueOf(metadata.get("order_id").asText());

                // Update payment status
                Optional<Payment> paymentOpt = paymentRepository.findById(internalPaymentId);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.updateStatus(PaymentStatus.CANCELLED);
                    paymentRepository.save(payment);

                    logger.info("Payment {} marked as canceled", internalPaymentId);
                }

                // Update order status
                Optional<Order> orderOpt = orderRepository.findById(orderId);
                if (orderOpt.isPresent()) {
                    Order order = orderOpt.get();
                    order.cancel();
                    orderRepository.save(order);

                    logger.info("Order {} marked as canceled", orderId);
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment canceled event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to handle payment canceled event", e);
        }
    }

    private void handlePaymentProcessing(ProcessWebhookEventRequest request) {
        logger.info("Handling payment processing event: {}", request.getEventId());

        try {
            JsonNode eventData = objectMapper.readTree(request.getPayload());
            JsonNode paymentIntentData = eventData.get("data").get("object");

            JsonNode metadata = paymentIntentData.get("metadata");

            if (metadata != null && metadata.has("internal_payment_id")) {
                Long internalPaymentId = Long.valueOf(metadata.get("internal_payment_id").asText());

                // Update payment status to processing
                Optional<Payment> paymentOpt = paymentRepository.findById(internalPaymentId);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.updateStatus(PaymentStatus.PROCESSING);
                    paymentRepository.save(payment);

                    logger.info("Payment {} marked as processing", internalPaymentId);
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment processing event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to handle payment processing event", e);
        }
    }
}
