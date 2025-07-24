package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.CartItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GetCartResponse {
    private Long id;
    private Long userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetCartResponse(Long id, Long userId, List<CartItem> items, BigDecimal totalAmount,
                         Integer totalItems, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Integer getTotalItems() { return totalItems; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}