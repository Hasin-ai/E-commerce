package com.ecommerce.core.usecase.cart;

public class AddCartItemRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;

    public AddCartItemRequest(Long userId, Long productId, Integer quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters
    public Long getUserId() { return userId; }
    public Long getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }
}