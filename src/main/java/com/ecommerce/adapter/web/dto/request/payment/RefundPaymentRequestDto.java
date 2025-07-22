package com.ecommerce.adapter.web.dto.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RefundPaymentRequestDto {

    @JsonProperty("paymentId")
    @NotNull(message = "Payment ID is required")
    private Long paymentId;

    @JsonProperty("orderId")
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @JsonProperty("reason")
    @NotBlank(message = "Refund reason is required")
    private String reason;

    @JsonProperty("refundType")
    @NotBlank(message = "Refund type is required")
    private String refundType; // FULL, PARTIAL

    @JsonProperty("amount")
    private java.math.BigDecimal amount; // Required for partial refunds

    @JsonProperty("notes")
    private String notes;

    // Default constructor
    public RefundPaymentRequestDto() {}

    // Constructor
    public RefundPaymentRequestDto(Long paymentId, Long orderId, String reason,
                                 String refundType, java.math.BigDecimal amount, String notes) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.reason = reason;
        this.refundType = refundType;
        this.amount = amount;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
