package com.ecommerce.adapter.persistence.jpa;

import com.ecommerce.adapter.persistence.entity.InventoryMovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMovementJpaRepository extends JpaRepository<InventoryMovementEntity, Long> {
    List<InventoryMovementEntity> findByProductIdOrderByCreatedAtDesc(Long productId);
    List<InventoryMovementEntity> findByInventoryIdOrderByCreatedAtDesc(Long inventoryId);
}