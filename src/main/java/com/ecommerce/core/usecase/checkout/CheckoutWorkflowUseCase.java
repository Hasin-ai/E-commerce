package com.ecommerce.core.usecase.checkout;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.order.entity.Address;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.payment.valueobject.PaymentMethod;
import com.ecommerce.infrastructure.external.payment.PaymentGatewayService;
import com.ecommerce.infrastructure.external.payment.PaymentGatewayRequest;
import com.ecommerce.infrastructure.external.payment.PaymentGatewayResponse;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Complete checkout workflow that handles:
 * 1. Cart validation
 * 2. Order creation
 * 3. Payment processing
 * 4. Cart cleanup
 */
@Service
@Transactional
public class CheckoutWorkflowUseCase {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayService paymentGatewayService;

    public CheckoutWorkflowUseCase(CartRepository cartRepository,
                                 OrderRepository orderRepository,
                                 PaymentRepository paymentRepository,
                                 PaymentGatewayService paymentGatewayService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGatewayService = paymentGatewayService;
    }

    public CheckoutWorkflowResponse execute(CheckoutWorkflowRequest request) {
        // Step 1: Validate cart exists and is not empty
        Cart cart = cartRepository.findByUserId(request.getUserId())
            .orElseThrow(() -> new NotFoundException("Cart not found"));

        if (cart.isEmpty()) {
            throw new BusinessException("Cannot checkout with empty cart");
        }

        // Step 2: Create order from cart
        Address shippingAddress = new Address(
            request.getShippingAddress().getStreet(),
            request.getShippingAddress().getCity(),
            request.getShippingAddress().getState(),
            request.getShippingAddress().getZipCode(),
            request.getShippingAddress().getCountry()
        );

        Address billingAddress = new Address(
            request.getBillingAddress().getStreet(),
            request.getBillingAddress().getCity(),
            request.getBillingAddress().getState(),
            request.getBillingAddress().getZipCode(),
            request.getBillingAddress().getCountry()
        );

        String orderNumber = generateOrderNumber();
        Order order = new Order(orderNumber, request.getUserId(), cart, shippingAddress, billingAddress);
        order = orderRepository.save(order);

        // Step 3: Process payment
        String paymentId = generatePaymentId();
        Payment payment = new Payment(
            paymentId,
            order.getId(),
            request.getUserId(),
            order.getTotalAmount(),
            order.getCurrency(),
            PaymentMethod.valueOf(request.getPaymentMethod())
        );

        payment = paymentRepository.save(payment);

        try {
            // Mark payment as processing
            payment.markAsProcessing();
            payment = paymentRepository.save(payment);

            // Process payment through gateway
            PaymentGatewayResponse gatewayResponse = paymentGatewayService.processPayment(
                new PaymentGatewayRequest(
                    paymentId,
                    order.getId(),
                    order.getTotalAmount(),
                    order.getCurrency(),
                    request.getPaymentMethodId(),
                    request.getCustomerId()
                )
            );

            if (gatewayResponse.isSuccessful()) {
                // Mark payment as completed
                payment.markAsCompleted(gatewayResponse.getTransactionId(), gatewayResponse.getRawResponse());
                
                // Confirm the order
                order.confirm();
                orderRepository.save(order);
                
                // Clear the cart after successful checkout
                cart.clear();
                cartRepository.save(cart);
                
                return new CheckoutWorkflowResponse(
                    order.getId(),
                    order.getOrderNumber(),
                    payment.getId(),
                    payment.getPaymentId(),
                    payment.getTransactionId(),
                    order.getTotalAmount(),
                    order.getCurrency(),
                    "COMPLETED",
                    "Checkout completed successfully"
                );
            } else {
                // Mark payment as failed
                payment.markAsFailed(gatewayResponse.getRawResponse());
                
                // Cancel the order
                order.cancel();
                orderRepository.save(order);
                
                return new CheckoutWorkflowResponse(
                    order.getId(),
                    order.getOrderNumber(),
                    payment.getId(),
                    payment.getPaymentId(),
                    null,
                    order.getTotalAmount(),
                    order.getCurrency(),
                    "FAILED",
                    "Payment failed: " + gatewayResponse.getErrorMessage()
                );
            }
        } catch (Exception e) {
            // Mark payment as failed
            payment.markAsFailed("Gateway error: " + e.getMessage());
            
            // Cancel the order
            order.cancel();
            orderRepository.save(order);
            
            throw new BusinessException("Checkout failed: " + e.getMessage());
        } finally {
            paymentRepository.save(payment);
        }
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generatePaymentId() {
        return "PAY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}