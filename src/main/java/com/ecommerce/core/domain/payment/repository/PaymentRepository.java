package com.ecommerce.core.domain.payment.repository;

import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.valueobject.PaymentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Payment entity.
 * This interface defines the contract for data access operations related to payments.
 */
public interface PaymentRepository {
    
    /**
     * Saves a payment to the repository.
     *
     * @param payment The payment to save
     * @return The saved payment with generated ID
     */
    Payment save(Payment payment);
    
    /**
     * Finds a payment by its ID.
     *
     * @param id The ID of the payment to find
     * @return An Optional containing the payment if found, or empty if not found
     */
    Optional<Payment> findById(Long id);
    
    /**
     * Finds a payment by its payment intent ID.
     *
     * @param paymentIntentId The payment intent ID from the payment gateway
     * @return An Optional containing the payment if found, or empty if not found
     */
    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
    
    /**
     * Finds all payments for a specific order.
     *
     * @param orderId The ID of the order
     * @return A list of payments for the order
     */
    List<Payment> findByOrderId(Long orderId);
    
    /**
     * Finds all payments for a specific user.
     *
     * @param userId The ID of the user
     * @return A list of payments for the user
     */
    List<Payment> findByUserId(Long userId);
    
    /**
     * Finds all payments with a specific status.
     *
     * @param status The payment status to filter by
     * @return A list of payments with the specified status
     */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
     * Updates the status of a payment.
     *
     * @param id The ID of the payment to update
     * @param status The new status
     * @return The updated payment
     */
    Payment updateStatus(Long id, PaymentStatus status);
    
    /**
     * Deletes a payment from the repository.
     *
     * @param id The ID of the payment to delete
     */
    void delete(Long id);
}