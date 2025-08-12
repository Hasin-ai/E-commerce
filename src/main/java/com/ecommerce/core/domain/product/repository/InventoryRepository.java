package com.ecommerce.core.domain.product.repository;

import com.ecommerce.core.domain.product.entity.Inventory;
import com.ecommerce.core.domain.product.valueobject.SKU;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    Inventory save(Inventory inventory);

    Optional<Inventory> findById(Long id);

    Optional<Inventory> findBySku(SKU sku);

    List<Inventory> findAll();

    List<Inventory> findByQuantityLessThanEqual(int quantity);

    List<Inventory> findLowStockItems();

    List<Inventory> findItemsNeedingReorder();

    void delete(Inventory inventory);

    void deleteById(Long id);

    boolean existsBySku(SKU sku);
}
