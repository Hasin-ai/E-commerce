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
public class InventoryMovement {
    private Long id;
    private Long inventoryId;
    private Long productId;
    private MovementType type;
    private int quantity;
    private int previousQuantity;
    private int newQuantity;
    private String reason;
    private String reference;
    private Long userId;
    private LocalDateTime createdAt;
}