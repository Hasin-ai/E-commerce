package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

public class AddCartItemResponse {
    private Long cartId;
    private Long userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private String message;

    public AddCartItemResponse(Long cartId, Long userId, List<CartItem> items, BigDecimal totalAmount,
                             Integer totalItems, String message) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.message = message;
    }

    // Getters
    public Long getCartId() { return cartId; }
    public Long getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Integer getTotalItems() { return totalItems; }
    public String getMessage() { return message; }
}