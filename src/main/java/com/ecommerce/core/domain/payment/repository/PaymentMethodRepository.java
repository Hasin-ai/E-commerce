package com.ecommerce.core.domain.payment.repository;

import com.ecommerce.core.domain.payment.entity.PaymentMethod;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for PaymentMethod entity.
 * This interface defines the contract for data access operations related to payment methods.
 */
public interface PaymentMethodRepository {
    
    /**
     * Saves a payment method to the repository.
     *
     * @param paymentMethod The payment method to save
     * @return The saved payment method with generated ID
     */
    PaymentMethod save(PaymentMethod paymentMethod);
    
    /**
     * Finds a payment method by its ID.
     *
     * @param id The ID of the payment method to find
     * @return An Optional containing the payment method if found, or empty if not found
     */
    Optional<PaymentMethod> findById(Long id);
    
    /**
     * Finds a payment method by its Stripe payment method ID.
     *
     * @param stripePaymentMethodId The Stripe payment method ID
     * @return An Optional containing the payment method if found, or empty if not found
     */
    Optional<PaymentMethod> findByStripePaymentMethodId(String stripePaymentMethodId);
    
    /**
     * Finds all payment methods for a specific user.
     *
     * @param userId The ID of the user
     * @return A list of payment methods for the user
     */
    List<PaymentMethod> findByUserId(Long userId);
    
    /**
     * Finds the default payment method for a specific user.
     *
     * @param userId The ID of the user
     * @return An Optional containing the default payment method if found, or empty if not found
     */
    Optional<PaymentMethod> findDefaultByUserId(Long userId);
    
    /**
     * Sets a payment method as the default for a user.
     * This method should also unset any previously default payment method.
     *
     * @param id The ID of the payment method to set as default
     * @param userId The ID of the user
     * @return The updated payment method
     */
    PaymentMethod setAsDefault(Long id, Long userId);
    
    /**
     * Updates the expiry details of a payment method.
     *
     * @param id The ID of the payment method to update
     * @param expiryMonth The new expiry month
     * @param expiryYear The new expiry year
     * @return The updated payment method
     */
    PaymentMethod updateExpiry(Long id, Integer expiryMonth, Integer expiryYear);
    
    /**
     * Updates the billing address of a payment method.
     *
     * @param id The ID of the payment method to update
     * @param address The new billing address
     * @param city The new billing city
     * @param state The new billing state
     * @param zip The new billing zip code
     * @param country The new billing country
     * @return The updated payment method
     */
    PaymentMethod updateBillingAddress(Long id, String address, String city, String state, String zip, String country);
    
    /**
     * Deletes a payment method from the repository.
     *
     * @param id The ID of the payment method to delete
     */
    void delete(Long id);
}