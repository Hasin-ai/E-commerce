package com.ecommerce.core.usecase.cart;

public class GetCartRequest {
    private Long userId;

    public GetCartRequest(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}