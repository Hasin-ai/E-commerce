package com.ecommerce.core.usecase.checkout;

import java.math.BigDecimal;

public class CheckoutWorkflowResponse {
    private Long orderId;
    private String orderNumber;
    private Long paymentId;
    private String paymentReference;
    private String transactionId;
    private BigDecimal totalAmount;
    private String currency;
    private String status;
    private String message;

    public CheckoutWorkflowResponse(Long orderId, String orderNumber, Long paymentId, String paymentReference,
                                  String transactionId, BigDecimal totalAmount, String currency, 
                                  String status, String message) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.paymentId = paymentId;
        this.paymentReference = paymentReference;
        this.transactionId = transactionId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.status = status;
        this.message = message;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public Long getPaymentId() { return paymentId; }
    public String getPaymentReference() { return paymentReference; }
    public String getTransactionId() { return transactionId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}