package com.ecommerce.core.domain.payment.repository;

import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByUserIdAndStatus(Long userId, PaymentStatus status);
    Optional<Payment> findByTransactionId(String transactionId);
}
