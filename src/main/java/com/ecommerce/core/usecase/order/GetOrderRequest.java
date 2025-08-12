package com.ecommerce.core.usecase.order;

public class GetOrderRequest {
    private Long orderId;
    private Long userId;

    public GetOrderRequest(Long orderId, Long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
}