package com.ecommerce.core.domain.payment.entity;

import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a payment in the system.
 * This is the core domain entity for the payment module.
 */
@Getter
@Setter
public class Payment {
    private Long id;
    private Long orderId;
    private String paymentIntentId;
    private Price amount;
    private PaymentStatus status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String currency;
    private String description;
    private String customerEmail;
    private String errorMessage;

    /**
     * Creates a new payment for an order.
     *
     * @param orderId The ID of the order associated with this payment
     * @param amount The amount to be paid
     * @param currency The currency of the payment
     * @param customerEmail The email of the customer making the payment
     */
    public Payment(Long orderId, Price amount, String currency, String customerEmail) {
        if (orderId == null) {
            throw new BusinessException("Order ID cannot be null");
        }
        if (amount == null || amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Payment amount must be greater than zero");
        }
        if (currency == null || currency.isEmpty()) {
            throw new BusinessException("Currency cannot be null or empty");
        }

        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.customerEmail = customerEmail;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the payment status and sets the payment intent ID.
     *
     * @param paymentIntentId The payment intent ID from the payment gateway
     * @param status The new status of the payment
     */
    public void updatePaymentIntent(String paymentIntentId, PaymentStatus status) {
        this.paymentIntentId = paymentIntentId;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the payment as completed.
     */
    public void markAsCompleted() {
        if (this.status != PaymentStatus.PROCESSING) {
            throw new BusinessException("Only processing payments can be marked as completed");
        }
        this.status = PaymentStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the payment as failed with an error message.
     *
     * @param errorMessage The error message describing why the payment failed
     */
    public void markAsFailed(String errorMessage) {
        this.status = PaymentStatus.FAILED;
        this.errorMessage = errorMessage;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks the payment as refunded.
     */
    public void refund() {
        if (this.status != PaymentStatus.COMPLETED) {
            throw new BusinessException("Only completed payments can be refunded");
        }
        this.status = PaymentStatus.REFUNDED;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Cancels the payment.
     */
    public void cancel() {
        if (this.status == PaymentStatus.COMPLETED || this.status == PaymentStatus.REFUNDED) {
            throw new BusinessException("Cannot cancel a completed or refunded payment");
        }
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
}