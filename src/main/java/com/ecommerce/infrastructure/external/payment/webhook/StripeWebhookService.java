package com.ecommerce.infrastructure.external.payment.webhook;

import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class StripeWebhookService {

    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookService.class);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public StripeWebhookService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public void processWebhookEvent(Event event) {
        logger.info("Processing Stripe webhook event: {}", event.getType());

        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentSucceeded(event);
                break;
            case "payment_intent.payment_failed":
                handlePaymentFailed(event);
                break;
            case "payment_intent.requires_action":
                handlePaymentRequiresAction(event);
                break;
            case "payment_intent.canceled":
                handlePaymentCanceled(event);
                break;
            case "charge.dispute.created":
                handleChargeDispute(event);
                break;
            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;
            case "checkout.session.expired":
                handleCheckoutSessionExpired(event);
                break;
            default:
                logger.info("Unhandled event type: {}", event.getType());
        }
    }

    private void handlePaymentSucceeded(Event event) {
        try {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            if (stripeObject instanceof PaymentIntent paymentIntent) {

                Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntent.getId());
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.markAsCompleted(paymentIntent.getId(), paymentIntent.toJson());
                    paymentRepository.save(payment);

                    // Update order status to confirmed/processing when payment succeeds
                    String orderIdStr = paymentIntent.getMetadata().get("order_id");
                    if (orderIdStr != null) {
                        try {
                            Long orderId = Long.parseLong(orderIdStr);
                            Optional<Order> orderOpt = orderRepository.findById(orderId);
                            if (orderOpt.isPresent()) {
                                Order order = orderOpt.get();
                                order.confirm();
                                orderRepository.save(order);
                                logger.info("Order {} status updated to CONFIRMED after successful payment", orderId);
                            }
                        } catch (NumberFormatException e) {
                            logger.warn("Invalid order ID in payment metadata: {}", orderIdStr);
                        }
                    }

                    logger.info("Payment completed successfully: {}", paymentIntent.getId());
                } else {
                    logger.warn("Payment not found for transaction ID: {}", paymentIntent.getId());
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment succeeded event", e);
            throw new RuntimeException("Failed to process payment succeeded event", e);
        }
    }

    private void handlePaymentFailed(Event event) {
        try {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            if (stripeObject instanceof PaymentIntent paymentIntent) {

                Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntent.getId());
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.markAsFailed(paymentIntent.toJson());
                    paymentRepository.save(payment);

                    // Update order status to cancelled when payment fails
                    String orderIdStr = paymentIntent.getMetadata().get("order_id");
                    if (orderIdStr != null) {
                        try {
                            Long orderId = Long.parseLong(orderIdStr);
                            Optional<Order> orderOpt = orderRepository.findById(orderId);
                            if (orderOpt.isPresent()) {
                                Order order = orderOpt.get();
                                order.cancel();
                                orderRepository.save(order);
                                logger.info("Order {} status updated to CANCELLED after failed payment", orderId);
                            }
                        } catch (NumberFormatException e) {
                            logger.warn("Invalid order ID in payment metadata: {}", orderIdStr);
                        }
                    }

                    logger.info("Payment failed: {}", paymentIntent.getId());
                } else {
                    logger.warn("Payment not found for transaction ID: {}", paymentIntent.getId());
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment failed event", e);
            throw new RuntimeException("Failed to process payment failed event", e);
        }
    }

    private void handlePaymentRequiresAction(Event event) {
        try {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            if (stripeObject instanceof PaymentIntent paymentIntent) {

                Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntent.getId());
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.updateStatus(PaymentStatus.REQUIRES_ACTION);
                    paymentRepository.save(payment);

                    logger.info("Payment requires action: {}", paymentIntent.getId());
                } else {
                    logger.warn("Payment not found for transaction ID: {}", paymentIntent.getId());
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment requires action event", e);
            throw new RuntimeException("Failed to process payment requires action event", e);
        }
    }

    private void handlePaymentCanceled(Event event) {
        try {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            if (stripeObject instanceof PaymentIntent paymentIntent) {

                Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntent.getId());
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.updateStatus(PaymentStatus.CANCELLED);
                    paymentRepository.save(payment);

                    // Update order status when payment is cancelled
                    String orderIdStr = paymentIntent.getMetadata().get("order_id");
                    if (orderIdStr != null) {
                        try {
                            Long orderId = Long.parseLong(orderIdStr);
                            Optional<Order> orderOpt = orderRepository.findById(orderId);
                            if (orderOpt.isPresent()) {
                                Order order = orderOpt.get();
                                order.cancel();
                                orderRepository.save(order);
                                logger.info("Order {} status updated to CANCELLED after payment cancellation", orderId);
                            }
                        } catch (NumberFormatException e) {
                            logger.warn("Invalid order ID in payment metadata: {}", orderIdStr);
                        }
                    }

                    logger.info("Payment cancelled: {}", paymentIntent.getId());
                } else {
                    logger.warn("Payment not found for transaction ID: {}", paymentIntent.getId());
                }
            }
        } catch (Exception e) {
            logger.error("Error handling payment cancelled event", e);
            throw new RuntimeException("Failed to process payment cancelled event", e);
        }
    }

    private void handleChargeDispute(Event event) {
        try {
            // Handle charge dispute - mark payment as disputed
            logger.info("Charge dispute created for event: {}", event.getId());

            // You might want to implement additional logic here like:
            // - Notify administrators
            // - Update payment status to disputed
            // - Send notifications to customer

        } catch (Exception e) {
            logger.error("Error handling charge dispute event", e);
            throw new RuntimeException("Failed to process charge dispute event", e);
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {
        try {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            if (stripeObject instanceof com.stripe.model.checkout.Session session) {

                String paymentIntentId = session.getPaymentIntent();
                if (paymentIntentId != null) {
                    Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntentId);
                    if (paymentOpt.isPresent()) {
                        Payment payment = paymentOpt.get();
                        payment.updateStatus(PaymentStatus.COMPLETED);
                        paymentRepository.save(payment);

                        logger.info("Checkout session completed for payment: {}", paymentIntentId);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error handling checkout session completed event", e);
            throw new RuntimeException("Failed to process checkout session completed event", e);
        }
    }

    private void handleCheckoutSessionExpired(Event event) {
        try {
            StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            if (stripeObject instanceof com.stripe.model.checkout.Session session) {

                String paymentIntentId = session.getPaymentIntent();
                if (paymentIntentId != null) {
                    Optional<Payment> paymentOpt = paymentRepository.findByTransactionId(paymentIntentId);
                    if (paymentOpt.isPresent()) {
                        Payment payment = paymentOpt.get();
                        payment.updateStatus(PaymentStatus.CANCELLED);
                        paymentRepository.save(payment);

                        logger.info("Checkout session expired for payment: {}", paymentIntentId);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error handling checkout session expired event", e);
            throw new RuntimeException("Failed to process checkout session expired event", e);
        }
    }
}
