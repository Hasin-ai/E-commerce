package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.adapter.persistence.jpa.entity.PaymentJpaEntity;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentJpaEntity, Long> {

    Optional<PaymentJpaEntity> findByPaymentIntentId(String paymentIntentId);

    List<PaymentJpaEntity> findByOrderId(Long orderId);

    List<PaymentJpaEntity> findByUserId(Long userId);

    List<PaymentJpaEntity> findByStatus(PaymentStatus status);

    boolean existsByPaymentIntentId(String paymentIntentId);
}
