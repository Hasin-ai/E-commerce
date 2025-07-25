package com.ecommerce.core.usecase.payment;

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

@Service
@Transactional
public class ProcessPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGatewayService paymentGatewayService;

    public ProcessPaymentUseCase(PaymentRepository paymentRepository, 
                               OrderRepository orderRepository,
                               PaymentGatewayService paymentGatewayService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentGatewayService = paymentGatewayService;
    }

    public ProcessPaymentResponse execute(ProcessPaymentRequest request) {
        // Validate order exists and belongs to user
        Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!order.getUserId().equals(request.getUserId())) {
            throw new BusinessException("Order does not belong to user");
        }

        // Validate order is in correct state for payment
        if (order.getStatus() != com.ecommerce.core.domain.order.entity.OrderStatus.PENDING) {
            throw new BusinessException("Order is not in a payable state");
        }

        // Validate payment amount matches order total
        if (request.getAmount().compareTo(order.getTotalAmount()) != 0) {
            throw new BusinessException("Payment amount does not match order total");
        }

        // Create payment entity
        String paymentId = generatePaymentId();
        Payment payment = new Payment(
            paymentId,
            order.getId(),
            request.getUserId(),
            request.getAmount(),
            request.getCurrency(),
            PaymentMethod.valueOf(request.getPaymentMethod())
        );

        // Save payment in pending state
        payment = paymentRepository.save(payment);

        try {
            // Mark payment as processing
            payment.markAsProcessing();
            payment = paymentRepository.save(payment);

            // Process payment through gateway
            PaymentGatewayResponse gatewayResponse = paymentGatewayService.processPayment(
                new PaymentGatewayRequest(
                    paymentId,
                    request.getOrderId(),
                    request.getAmount(),
                    request.getCurrency(),
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
                
                return new ProcessPaymentResponse(
                    payment.getId(),
                    payment.getPaymentId(),
                    payment.getStatus().name(),
                    payment.getTransactionId(),
                    "Payment processed successfully"
                );
            } else {
                // Mark payment as failed
                payment.markAsFailed(gatewayResponse.getRawResponse());
                
                return new ProcessPaymentResponse(
                    payment.getId(),
                    payment.getPaymentId(),
                    payment.getStatus().name(),
                    null,
                    gatewayResponse.getErrorMessage()
                );
            }
        } catch (Exception e) {
            // Mark payment as failed
            payment.markAsFailed("Gateway error: " + e.getMessage());
            
            throw new BusinessException("Payment processing failed: " + e.getMessage());
        } finally {
            paymentRepository.save(payment);
        }
    }

    private String generatePaymentId() {
        return "PAY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}