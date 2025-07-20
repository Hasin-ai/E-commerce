package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.jpa.entity.PaymentJpaEntity;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.product.valueobject.Price;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityMapper {

    public PaymentJpaEntity toJpaEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentJpaEntity jpaEntity = new PaymentJpaEntity();
        jpaEntity.setId(payment.getId());
        jpaEntity.setOrderId(payment.getOrderId());
        // Note: userId is not available in domain entity, setting to null for now
        jpaEntity.setUserId(null);
        jpaEntity.setPaymentIntentId(payment.getPaymentIntentId());
        jpaEntity.setAmount(payment.getAmount() != null ? payment.getAmount().getAmount() : null);
        jpaEntity.setCurrency(payment.getCurrency());
        jpaEntity.setStatus(payment.getStatus());
        jpaEntity.setPaymentMethod(payment.getPaymentMethod());
        // Note: cardLastFour and cardBrand are not available in domain entity
        jpaEntity.setCardLastFour(null);
        jpaEntity.setCardBrand(null);
        jpaEntity.setDescription(payment.getDescription());
        jpaEntity.setCustomerEmail(payment.getCustomerEmail());
        jpaEntity.setErrorMessage(payment.getErrorMessage());
        jpaEntity.setCreatedAt(payment.getCreatedAt());
        jpaEntity.setUpdatedAt(payment.getUpdatedAt());

        return jpaEntity;
    }

    public Payment toDomainEntity(PaymentJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        // Create payment using the available constructor
        Price price = jpaEntity.getAmount() != null ?
            new Price(jpaEntity.getAmount(), jpaEntity.getCurrency()) : null;

        Payment payment = new Payment(
            jpaEntity.getOrderId(),
            price,
            jpaEntity.getCurrency(),
            jpaEntity.getCustomerEmail()
        );

        // Set additional fields
        payment.setId(jpaEntity.getId());
        payment.setPaymentIntentId(jpaEntity.getPaymentIntentId());
        payment.setStatus(jpaEntity.getStatus());
        payment.setPaymentMethod(jpaEntity.getPaymentMethod());
        payment.setDescription(jpaEntity.getDescription());
        payment.setErrorMessage(jpaEntity.getErrorMessage());
        payment.setCreatedAt(jpaEntity.getCreatedAt());
        payment.setUpdatedAt(jpaEntity.getUpdatedAt());

        return payment;
    }
}
