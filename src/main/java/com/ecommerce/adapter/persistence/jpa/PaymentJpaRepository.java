package com.ecommerce.adapter.persistence.jpa;

import com.ecommerce.adapter.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByPaymentId(String paymentId);
    List<PaymentEntity> findByOrderId(Long orderId);
    List<PaymentEntity> findByUserIdAndStatus(Long userId, String status);
    Optional<PaymentEntity> findByTransactionId(String transactionId);
}