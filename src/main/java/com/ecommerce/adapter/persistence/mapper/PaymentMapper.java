package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.valueobject.PaymentMethod;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.adapter.persistence.entity.PaymentEntity;

import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentEntity entity = new PaymentEntity();
        entity.setId(payment.getId());
        entity.setPaymentId(payment.getPaymentId());
        entity.setOrderId(payment.getOrderId());
        entity.setUserId(payment.getUserId());
        entity.setAmount(payment.getAmount());
        entity.setCurrency(payment.getCurrency());
        entity.setMethod(mapPaymentMethod(payment.getMethod()));
        entity.setStatus(mapPaymentStatus(payment.getStatus()));
        entity.setTransactionId(payment.getTransactionId());
        entity.setGatewayResponse(payment.getGatewayResponse());
        entity.setCreatedAt(payment.getCreatedAt());
        entity.setProcessedAt(payment.getProcessedAt());

        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        Payment payment = new Payment(
            entity.getPaymentId(),
            entity.getOrderId(),
            entity.getUserId(),
            entity.getAmount(),
            entity.getCurrency(),
            mapPaymentMethod(entity.getMethod())
        );

        // Use reflection to set private fields for persistence
        try {
            java.lang.reflect.Field idField = Payment.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(payment, entity.getId());

            java.lang.reflect.Field statusField = Payment.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(payment, mapPaymentStatus(entity.getStatus()));

            java.lang.reflect.Field transactionIdField = Payment.class.getDeclaredField("transactionId");
            transactionIdField.setAccessible(true);
            transactionIdField.set(payment, entity.getTransactionId());

            java.lang.reflect.Field gatewayResponseField = Payment.class.getDeclaredField("gatewayResponse");
            gatewayResponseField.setAccessible(true);
            gatewayResponseField.set(payment, entity.getGatewayResponse());

            java.lang.reflect.Field createdAtField = Payment.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(payment, entity.getCreatedAt());

            java.lang.reflect.Field processedAtField = Payment.class.getDeclaredField("processedAt");
            processedAtField.setAccessible(true);
            processedAtField.set(payment, entity.getProcessedAt());

        } catch (Exception e) {
            throw new RuntimeException("Error mapping payment entity to domain", e);
        }

        return payment;
    }

    private PaymentEntity.PaymentMethodEntity mapPaymentMethod(PaymentMethod method) {
        if (method == null) {
            return null;
        }
        return PaymentEntity.PaymentMethodEntity.valueOf(method.name());
    }

    private PaymentMethod mapPaymentMethod(PaymentEntity.PaymentMethodEntity method) {
        if (method == null) {
            return null;
        }
        return PaymentMethod.valueOf(method.name());
    }

    private PaymentEntity.PaymentStatusEntity mapPaymentStatus(PaymentStatus status) {
        if (status == null) {
            return null;
        }
        return PaymentEntity.PaymentStatusEntity.valueOf(status.name());
    }

    private PaymentStatus mapPaymentStatus(PaymentEntity.PaymentStatusEntity status) {
        if (status == null) {
            return null;
        }
        return PaymentStatus.valueOf(status.name());
    }
}