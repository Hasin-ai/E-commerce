package com.ecommerce.core.domain.payment.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setAmount(BigDecimal.valueOf(199.99));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMethod(PaymentMethod.CREDIT_CARD);
        payment.setTransactionId("TXN-12345");
        payment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create payment with valid properties")
    void shouldCreatePaymentWithValidProperties() {
        assertNotNull(payment);
        assertEquals(1L, payment.getId());
        assertEquals(1L, payment.getOrderId());
        assertEquals(BigDecimal.valueOf(199.99), payment.getAmount());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertEquals(PaymentMethod.CREDIT_CARD, payment.getMethod());
        assertEquals("TXN-12345", payment.getTransactionId());
        assertNotNull(payment.getCreatedAt());
    }

    @Test
    @DisplayName("Should handle payment status transitions")
    void shouldHandlePaymentStatusTransitions() {
        assertEquals(PaymentStatus.PENDING, payment.getStatus());

        payment.setStatus(PaymentStatus.PROCESSING);
        assertEquals(PaymentStatus.PROCESSING, payment.getStatus());

        payment.setStatus(PaymentStatus.COMPLETED);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    @DisplayName("Should handle failed payment")
    void shouldHandleFailedPayment() {
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailureReason("Insufficient funds");

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        assertEquals("Insufficient funds", payment.getFailureReason());
    }

    @Test
    @DisplayName("Should handle different payment methods")
    void shouldHandleDifferentPaymentMethods() {
        payment.setMethod(PaymentMethod.PAYPAL);
        assertEquals(PaymentMethod.PAYPAL, payment.getMethod());

        payment.setMethod(PaymentMethod.BANK_TRANSFER);
        assertEquals(PaymentMethod.BANK_TRANSFER, payment.getMethod());

        payment.setMethod(PaymentMethod.DIGITAL_WALLET);
        assertEquals(PaymentMethod.DIGITAL_WALLET, payment.getMethod());
    }

    @Test
    @DisplayName("Should validate payment amount")
    void shouldValidatePaymentAmount() {
        assertTrue(payment.getAmount().compareTo(BigDecimal.ZERO) > 0);
        
        payment.setAmount(BigDecimal.valueOf(0.01));
        assertTrue(payment.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Should handle payment refund")
    void shouldHandlePaymentRefund() {
        payment.setStatus(PaymentStatus.COMPLETED);
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundAmount(BigDecimal.valueOf(50.00));

        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
        assertEquals(BigDecimal.valueOf(50.00), payment.getRefundAmount());
    }
}
