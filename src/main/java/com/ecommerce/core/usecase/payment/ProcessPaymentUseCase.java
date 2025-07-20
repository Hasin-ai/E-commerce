package com.ecommerce.core.usecase.payment;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.entity.Transaction;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.payment.repository.TransactionRepository;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Use case for processing a payment.
 * This use case is responsible for processing a payment after a user has completed the payment flow.
 */
@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final StripeService stripeService;

    /**
     * Processes a payment based on the payment intent ID.
     *
     * @param request The request containing the payment intent ID
     * @return The response containing the result of the payment processing
     */
    @Transactional
    public ProcessPaymentResponse execute(ProcessPaymentRequest request) {
        validateRequest(request);

        // Find the payment by payment intent ID
        Payment payment = paymentRepository.findByPaymentIntentId(request.getPaymentIntentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with payment intent ID: " + request.getPaymentIntentId()));

        // Verify the payment with Stripe
        StripeService.VerifyPaymentResponse verifyResponse = stripeService.verifyPayment(request.getPaymentIntentId());

        // Create a transaction record
        Transaction transaction = new Transaction(
                payment.getId(),
                payment.getAmount().getAmount(),
                payment.getCurrency(),
                request.getPaymentMethod(),
                "stripe"
        );

        // Process the payment based on the verification result
        if (verifyResponse.isSuccessful()) {
            // Mark the payment as completed
            payment.markAsCompleted();
            paymentRepository.save(payment);

            // Mark the transaction as successful
            transaction.markAsSuccessful(
                    request.getPaymentIntentId(),
                    verifyResponse.getStatus(),
                    request.getCardLastFour(),
                    request.getCardBrand()
            );
            transactionRepository.save(transaction);

            // Update the order status
            Order order = orderRepository.findById(payment.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + payment.getOrderId()));
            order.confirm();
            orderRepository.save(order);

            return ProcessPaymentResponse.builder()
                    .successful(true)
                    .paymentId(payment.getId())
                    .orderId(order.getId())
                    .transactionId(transaction.getId())
                    .message("Payment processed successfully")
                    .build();
        } else {
            // Mark the payment as failed
            payment.markAsFailed(verifyResponse.getErrorMessage());
            paymentRepository.save(payment);

            // Mark the transaction as failed
            transaction.markAsFailed("payment_failed", verifyResponse.getErrorMessage());
            transactionRepository.save(transaction);

            return ProcessPaymentResponse.builder()
                    .successful(false)
                    .paymentId(payment.getId())
                    .orderId(payment.getOrderId())
                    .transactionId(transaction.getId())
                    .message(verifyResponse.getErrorMessage())
                    .build();
        }
    }

    private void validateRequest(ProcessPaymentRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getPaymentIntentId() == null || request.getPaymentIntentId().isEmpty()) {
            throw new BusinessException("Payment intent ID is required");
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            throw new BusinessException("Payment method is required");
        }
    }

    /**
     * Request object for processing a payment.
     */
    @Data
    @Builder
    public static class ProcessPaymentRequest {
        private String paymentIntentId;
        private String paymentMethod;
        private String cardLastFour;
        private String cardBrand;
    }

    /**
     * Response object for processing a payment.
     */
    @Data
    @Builder
    public static class ProcessPaymentResponse {
        private boolean successful;
        private Long paymentId;
        private Long orderId;
        private Long transactionId;
        private String message;
    }
}