package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProcessPaymentRequestDto {

    @NotNull(message = "Order ID is required")
    @JsonProperty("orderId")
    private Long orderId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 2 decimal places")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter code")
    private String currency;

    @NotBlank(message = "Payment method ID is required")
    @JsonProperty("paymentMethodId")
    private String paymentMethodId; // Stripe payment method ID (pm_xxxxx)

    @JsonProperty("paymentMethod")
    private String paymentMethod; // STRIPE, PAYPAL, etc.

    // Optional fields for additional payment context
    @JsonProperty("customerId")
    private String customerId; // Stripe customer ID

    @JsonProperty("savePaymentMethod")
    private Boolean savePaymentMethod = false;

    @JsonProperty("confirmPayment")
    private Boolean confirmPayment = true;

    // Constructors
    public ProcessPaymentRequestDto() {}

    // Getters and setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public Boolean getSavePaymentMethod() { return savePaymentMethod; }
    public void setSavePaymentMethod(Boolean savePaymentMethod) { this.savePaymentMethod = savePaymentMethod; }

    public Boolean getConfirmPayment() { return confirmPayment; }
    public void setConfirmPayment(Boolean confirmPayment) { this.confirmPayment = confirmPayment; }
}