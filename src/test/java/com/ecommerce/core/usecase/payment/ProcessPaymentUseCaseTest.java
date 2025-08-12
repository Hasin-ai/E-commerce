package com.ecommerce.core.usecase.payment;

import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.entity.PaymentStatus;
import com.ecommerce.core.domain.payment.entity.PaymentMethod;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentUseCaseTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ProcessPaymentUseCase processPaymentUseCase;

    private ProcessPaymentRequest request;
    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setTotalAmount(BigDecimal.valueOf(199.99));

        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setAmount(BigDecimal.valueOf(199.99));
        payment.setStatus(PaymentStatus.PENDING);

        request = new ProcessPaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        request.setAmount(BigDecimal.valueOf(199.99));
        request.setCardToken("card_token_123");
    }

    @Test
    @DisplayName("Should process payment successfully")
    void shouldProcessPaymentSuccessfully() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        ProcessPaymentResponse response = processPaymentUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getPaymentId());
        verify(orderRepository).findById(1L);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should fail when order not found")
    void shouldFailWhenOrderNotFound() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        ProcessPaymentResponse response = processPaymentUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Order not found", response.getErrorMessage());
        verify(orderRepository).findById(1L);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should fail when payment amount doesn't match order total")
    void shouldFailWhenPaymentAmountDoesntMatchOrderTotal() {
        // Given
        request.setAmount(BigDecimal.valueOf(100.00)); // Different from order total
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        ProcessPaymentResponse response = processPaymentUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Payment amount doesn't match order total", response.getErrorMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should handle payment processing failure")
    void shouldHandlePaymentProcessingFailure() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenThrow(new RuntimeException("Payment gateway error"));

        // When
        ProcessPaymentResponse response = processPaymentUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getErrorMessage().contains("error"));
    }

    @Test
    @DisplayName("Should validate payment method")
    void shouldValidatePaymentMethod() {
        // Given
        request.setPaymentMethod(null);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // When
        ProcessPaymentResponse response = processPaymentUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Payment method is required", response.getErrorMessage());
    }
}
