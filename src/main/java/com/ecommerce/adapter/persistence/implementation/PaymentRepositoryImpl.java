package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.entity.PaymentJpaEntity;
import com.ecommerce.adapter.persistence.jpa.repository.PaymentJpaRepository;
import com.ecommerce.adapter.persistence.mapper.PaymentEntityMapper;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentEntityMapper paymentEntityMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity jpaEntity = paymentEntityMapper.toJpaEntity(payment);
        PaymentJpaEntity savedEntity = paymentJpaRepository.save(jpaEntity);
        return paymentEntityMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentJpaRepository.findById(id)
                .map(paymentEntityMapper::toDomainEntity);
    }

    @Override
    public Optional<Payment> findByPaymentIntentId(String paymentIntentId) {
        return paymentJpaRepository.findByPaymentIntentId(paymentIntentId)
                .map(paymentEntityMapper::toDomainEntity);
    }

    @Override
    public List<Payment> findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId).stream()
                .map(paymentEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
        return paymentJpaRepository.findByUserId(userId).stream()
                .map(paymentEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentJpaRepository.findByStatus(status).stream()
                .map(paymentEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Payment updateStatus(Long id, PaymentStatus status) {
        Optional<PaymentJpaEntity> paymentOpt = paymentJpaRepository.findById(id);
        if (paymentOpt.isPresent()) {
            PaymentJpaEntity paymentEntity = paymentOpt.get();
            paymentEntity.setStatus(status);
            PaymentJpaEntity savedEntity = paymentJpaRepository.save(paymentEntity);
            return paymentEntityMapper.toDomainEntity(savedEntity);
        }
        throw new RuntimeException("Payment not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        paymentJpaRepository.deleteById(id);
    }
}
