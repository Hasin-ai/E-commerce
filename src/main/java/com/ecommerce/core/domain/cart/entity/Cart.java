package com.ecommerce.core.domain.cart.entity;

import com.ecommerce.shared.exception.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private Long id;
    private Long userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(Long productId, String productName, BigDecimal unitPrice, int quantity) {
        Optional<CartItem> existingItem = findItemByProductId(productId);

        if (existingItem.isPresent()) {
            existingItem.get().updateQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(productId, productName, unitPrice, quantity);
            items.add(newItem);
        }

        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateItemQuantity(Long productId, int quantity) {
        CartItem item = findItemByProductId(productId)
            .orElseThrow(() -> new ValidationException("Item not found in cart"));

        item.updateQuantity(quantity);
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void clear() {
        items.clear();
        this.totalAmount = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }

    public Optional<CartItem> findItemByProductId(Long productId) {
        return items.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst();
    }

    public void addItem(CartItem cartItem) {
        items.add(cartItem);
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTotal() {
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    public Object getUser() {
        return this.userId;
    }

    public BigDecimal getTotal() {
        return this.totalAmount;
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<CartItem> getItems() { return new ArrayList<>(items); }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }
    void setItems(List<CartItem> items) { this.items = items; }
}
