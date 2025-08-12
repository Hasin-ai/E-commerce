package com.ecommerce.core.domain.inventory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    private Long id;
    private Long productId;
    private Long vendorId;
    private String sku;
    private int quantity;
    private int reservedQuantity;
    private int minStockLevel;
    private int maxStockLevel;
    private String location;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    
    public int getAvailableQuantity() {
        return quantity - reservedQuantity;
    }
    
    public boolean isLowStock() {
        return getAvailableQuantity() <= minStockLevel;
    }
    
    public boolean isOutOfStock() {
        return getAvailableQuantity() <= 0;
    }
}