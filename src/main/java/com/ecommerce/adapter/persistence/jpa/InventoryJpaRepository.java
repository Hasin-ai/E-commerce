package com.ecommerce.adapter.persistence.jpa;

import com.ecommerce.adapter.persistence.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findByProductId(Long productId);
    Optional<InventoryEntity> findByProductIdAndVendorId(Long productId, Long vendorId);
    List<InventoryEntity> findByVendorId(Long vendorId);
    
    @Query("SELECT i FROM InventoryEntity i WHERE (i.quantity - i.reservedQuantity) <= i.minStockLevel")
    List<InventoryEntity> findLowStockItems();
    
    @Query("SELECT i FROM InventoryEntity i WHERE (i.quantity - i.reservedQuantity) <= 0")
    List<InventoryEntity> findOutOfStockItems();
    
    @Query("SELECT i FROM InventoryEntity i WHERE i.quantity <= :threshold")
    List<InventoryEntity> findByQuantityLessThanEqual(@Param("threshold") Integer threshold);
}