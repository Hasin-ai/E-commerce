package com.ecommerce.core.usecase.payment;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a payment session.
 * This use case is responsible for creating a payment session with Stripe.
 */
@Service
@RequiredArgsConstructor
public class CreatePaymentSessionUseCase {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    /**
     * Creates a payment session for an order.
     *
     * @param request The request containing the order ID and success/cancel URLs
     * @return The response containing the payment session ID and URL
     */
    @Transactional
    public CreatePaymentSessionResponse execute(CreatePaymentSessionRequest request) {
        validateRequest(request);

        // Retrieve the order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.getOrderId()));

        // Retrieve the user
        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + order.getUserId()));

        // Create a payment record
        Payment payment = new Payment(
                order.getId(),
                order.getTotal(),
                order.getTotal().getCurrency(),
                user.getEmail().getValue()
        );
        payment.setDescription("Payment for Order #" + order.getId());
        payment = paymentRepository.save(payment);

        // Create a payment session with Stripe
        StripeService.CreateSessionRequest stripeRequest = StripeService.CreateSessionRequest.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount().getAmount())
                .currency(payment.getCurrency())
                .description(payment.getDescription())
                .customerEmail(payment.getCustomerEmail())
                .successUrl(request.getSuccessUrl())
                .cancelUrl(request.getCancelUrl())
                .build();

        StripeService.CreateSessionResponse stripeResponse = stripeService.createPaymentSession(stripeRequest);

        // Update the payment with the payment intent ID
        payment.updatePaymentIntent(stripeResponse.getPaymentIntentId(), PaymentStatus.PROCESSING);
        paymentRepository.save(payment);

        return CreatePaymentSessionResponse.builder()
                .paymentId(payment.getId())
                .sessionId(stripeResponse.getSessionId())
                .sessionUrl(stripeResponse.getSessionUrl())
                .build();
    }

    private void validateRequest(CreatePaymentSessionRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getOrderId() == null) {
            throw new BusinessException("Order ID is required");
        }
        if (request.getSuccessUrl() == null || request.getSuccessUrl().isEmpty()) {
            throw new BusinessException("Success URL is required");
        }
        if (request.getCancelUrl() == null || request.getCancelUrl().isEmpty()) {
            throw new BusinessException("Cancel URL is required");
        }
    }

    /**
     * Request object for creating a payment session.
     */
    @Data
    @Builder
    public static class CreatePaymentSessionRequest {
        private Long orderId;
        private String successUrl;
        private String cancelUrl;
    }

    /**
     * Response object for creating a payment session.
     */
    @Data
    @Builder
    public static class CreatePaymentSessionResponse {
        private Long paymentId;
        private String sessionId;
        private String sessionUrl;
    }
}