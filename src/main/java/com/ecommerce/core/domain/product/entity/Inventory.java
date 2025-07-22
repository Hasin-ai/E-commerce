package com.ecommerce.core.domain.product.entity;

import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.shared.exception.BusinessException;

import java.time.LocalDateTime;

public class Inventory {
    private Long id;
    private SKU sku;
    private int quantity;
    private int reservedQuantity;
    private int reorderLevel;
    private LocalDateTime lastUpdated;

    public Inventory() {}

    public Inventory(SKU sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
        this.reservedQuantity = 0;
        this.reorderLevel = 0;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity < 0) {
            throw new BusinessException("Quantity cannot be negative");
        }
        this.quantity = newQuantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateReorderLevel(int reorderLevel) {
        if (reorderLevel < 0) {
            throw new BusinessException("Reorder level cannot be negative");
        }
        this.reorderLevel = reorderLevel;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean canReserve(int requestedQuantity) {
        return getAvailableQuantity() >= requestedQuantity;
    }

    public void reserve(int quantity) {
        if (!canReserve(quantity)) {
            throw new BusinessException("Insufficient inventory to reserve " + quantity + " items");
        }
        this.reservedQuantity += quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public void release(int quantity) {
        if (quantity > reservedQuantity) {
            throw new BusinessException("Cannot release more than reserved quantity");
        }
        this.reservedQuantity -= quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public int getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public boolean isInStock() {
        return getAvailableQuantity() > 0;
    }

    public boolean needsReorder() {
        return getAvailableQuantity() <= reorderLevel;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SKU getSku() { return sku; }
    public void setSku(SKU sku) { this.sku = sku; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public int getReorderLevel() { return reorderLevel; }
    public void setReorderLevel(int reorderLevel) { this.reorderLevel = reorderLevel; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
