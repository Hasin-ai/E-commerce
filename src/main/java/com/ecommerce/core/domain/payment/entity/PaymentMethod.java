package com.ecommerce.core.domain.payment.entity;

import com.ecommerce.shared.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents a payment method stored in the system.
 * This entity is used to store payment methods for users to reuse in future payments.
 */
@Getter
@Setter
public class PaymentMethod {
    private Long id;
    private Long userId;
    private String type;
    private String lastFourDigits;
    private String brand;
    private String holderName;
    private Integer expiryMonth;
    private Integer expiryYear;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private String billingCountry;
    private boolean isDefault;
    private String stripePaymentMethodId;
    private String gateway;
    private String gatewayMethodId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Creates a new payment method for a user.
     *
     * @param userId The ID of the user who owns this payment method
     * @param type The type of payment method (e.g., "card", "bank_account")
     * @param lastFourDigits The last four digits of the card or account
     * @param brand The brand of the card (e.g., "visa", "mastercard")
     * @param holderName The name of the card or account holder
     * @param stripePaymentMethodId The Stripe payment method ID
     */
    public PaymentMethod(Long userId, String type, String lastFourDigits, String brand, 
                         String holderName, String stripePaymentMethodId) {
        if (userId == null) {
            throw new BusinessException("User ID cannot be null");
        }
        if (type == null || type.isEmpty()) {
            throw new BusinessException("Payment method type cannot be null or empty");
        }
        if (stripePaymentMethodId == null || stripePaymentMethodId.isEmpty()) {
            throw new BusinessException("Stripe payment method ID cannot be null or empty");
        }

        this.userId = userId;
        this.type = type;
        this.lastFourDigits = lastFourDigits;
        this.brand = brand;
        this.holderName = holderName;
        this.stripePaymentMethodId = stripePaymentMethodId;
        this.gateway = "stripe";
        this.gatewayMethodId = stripePaymentMethodId;
        this.createdAt = LocalDateTime.now();
        this.isDefault = false;
    }

    /**
     * Sets this payment method as the default for the user.
     */
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the card expiry details.
     *
     * @param expiryMonth The expiry month (1-12)
     * @param expiryYear The expiry year (4-digit)
     */
    public void updateExpiry(Integer expiryMonth, Integer expiryYear) {
        if (expiryMonth == null || expiryMonth < 1 || expiryMonth > 12) {
            throw new BusinessException("Invalid expiry month");
        }
        if (expiryYear == null || expiryYear < LocalDateTime.now().getYear()) {
            throw new BusinessException("Invalid expiry year");
        }

        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the billing address details.
     *
     * @param address The billing address
     * @param city The billing city
     * @param state The billing state
     * @param zip The billing zip code
     * @param country The billing country
     */
    public void updateBillingAddress(String address, String city, String state, String zip, String country) {
        if (address == null || address.isEmpty()) {
            throw new BusinessException("Billing address cannot be null or empty");
        }
        if (country == null || country.isEmpty()) {
            throw new BusinessException("Billing country cannot be null or empty");
        }

        this.billingAddress = address;
        this.billingCity = city;
        this.billingState = state;
        this.billingZip = zip;
        this.billingCountry = country;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Checks if the payment method is expired.
     *
     * @return true if the payment method is expired, false otherwise
     */
    public boolean isExpired() {
        if (expiryMonth == null || expiryYear == null) {
            return false; // No expiry date set
        }

        LocalDateTime now = LocalDateTime.now();
        return (expiryYear < now.getYear()) || 
               (expiryYear == now.getYear() && expiryMonth < now.getMonthValue());
    }
}