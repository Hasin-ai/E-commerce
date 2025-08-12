package com.ecommerce.core.usecase.order;

import java.math.BigDecimal;

public class CreateOrderResponse {
    private Long orderId;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private String currency;
    private String message;

    public CreateOrderResponse(Long orderId, String orderNumber, String status, 
                             BigDecimal totalAmount, String currency, String message) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.status = status;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.message = message;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public String getMessage() { return message; }
}