package com.ecommerce.core.domain.inventory.repository;

import com.ecommerce.core.domain.inventory.entity.Inventory;
import com.ecommerce.core.domain.inventory.entity.InventoryMovement;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {
    Optional<Inventory> findByProductId(Long productId);
    Optional<Inventory> findByProductIdAndVendorId(Long productId, Long vendorId);
    List<Inventory> findLowStockItems();
    List<Inventory> findOutOfStockItems();
    Inventory save(Inventory inventory);
    void delete(Long id);
    
    // Movement tracking
    InventoryMovement saveMovement(InventoryMovement movement);
    List<InventoryMovement> findMovementsByProductId(Long productId);
    
    // Stock operations
    boolean reserveStock(Long productId, int quantity);
    boolean releaseStock(Long productId, int quantity);
    boolean adjustStock(Long productId, int newQuantity, String reason);
}