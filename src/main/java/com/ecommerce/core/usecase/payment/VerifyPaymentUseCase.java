package com.ecommerce.core.usecase.payment;

import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for verifying a payment status.
 * This use case is responsible for checking the status of a payment.
 */
@Service
@RequiredArgsConstructor
public class VerifyPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    /**
     * Verifies the status of a payment.
     *
     * @param request The request containing the payment ID or payment intent ID
     * @return The response containing the payment status
     */
    @Transactional(readOnly = true)
    public VerifyPaymentResponse execute(VerifyPaymentRequest request) {
        validateRequest(request);

        Payment payment;
        
        // Find the payment by ID or payment intent ID
        if (request.getPaymentId() != null) {
            payment = paymentRepository.findById(request.getPaymentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + request.getPaymentId()));
        } else {
            payment = paymentRepository.findByPaymentIntentId(request.getPaymentIntentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found with payment intent ID: " + request.getPaymentIntentId()));
        }

        // If the payment is still in processing status, verify with Stripe
        if (payment.getStatus().toString().equals("PROCESSING")) {
            StripeService.VerifyPaymentResponse stripeResponse = stripeService.verifyPayment(payment.getPaymentIntentId());
            
            // Update payment status if needed
            if (stripeResponse.isSuccessful() && !payment.getStatus().toString().equals("COMPLETED")) {
                payment.markAsCompleted();
                paymentRepository.save(payment);
            } else if (!stripeResponse.isSuccessful() && !payment.getStatus().toString().equals("FAILED")) {
                payment.markAsFailed(stripeResponse.getErrorMessage());
                paymentRepository.save(payment);
            }
            
            return VerifyPaymentResponse.builder()
                    .paymentId(payment.getId())
                    .orderId(payment.getOrderId())
                    .status(payment.getStatus().toString())
                    .amount(payment.getAmount().getAmount())
                    .currency(payment.getCurrency())
                    .paymentIntentId(payment.getPaymentIntentId())
                    .successful(stripeResponse.isSuccessful())
                    .message(stripeResponse.isSuccessful() ? "Payment verified successfully" : stripeResponse.getErrorMessage())
                    .build();
        }
        
        // Return the current payment status
        return VerifyPaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .status(payment.getStatus().toString())
                .amount(payment.getAmount().getAmount())
                .currency(payment.getCurrency())
                .paymentIntentId(payment.getPaymentIntentId())
                .successful(payment.getStatus().toString().equals("COMPLETED"))
                .message("Payment status: " + payment.getStatus().toString())
                .build();
    }

    private void validateRequest(VerifyPaymentRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getPaymentId() == null && (request.getPaymentIntentId() == null || request.getPaymentIntentId().isEmpty())) {
            throw new BusinessException("Either payment ID or payment intent ID is required");
        }
    }

    /**
     * Request object for verifying a payment.
     */
    @Data
    @Builder
    public static class VerifyPaymentRequest {
        private Long paymentId;
        private String paymentIntentId;
    }

    /**
     * Response object for verifying a payment.
     */
    @Data
    @Builder
    public static class VerifyPaymentResponse {
        private Long paymentId;
        private Long orderId;
        private String status;
        private java.math.BigDecimal amount;
        private String currency;
        private String paymentIntentId;
        private boolean successful;
        private String message;
    }
}