package com.ecommerce.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class CheckoutResponseDto {

    @JsonProperty("orderId")
    private Long orderId;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("paymentId")
    private Long paymentId;

    @JsonProperty("paymentReference")
    private String paymentReference;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    private String currency;

    private String status;

    // Constructors
    public CheckoutResponseDto() {}

    public CheckoutResponseDto(Long orderId, String orderNumber, Long paymentId, String paymentReference,
                             String transactionId, BigDecimal totalAmount, String currency, String status) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.paymentId = paymentId;
        this.paymentReference = paymentReference;
        this.transactionId = transactionId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.status = status;
    }

    // Getters and setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}