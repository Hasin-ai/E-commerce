package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.entity.InventoryEntity;
import com.ecommerce.adapter.persistence.entity.InventoryMovementEntity;
import com.ecommerce.adapter.persistence.jpa.InventoryJpaRepository;
import com.ecommerce.adapter.persistence.jpa.InventoryMovementJpaRepository;
import com.ecommerce.adapter.persistence.mapper.InventoryMapper;
import com.ecommerce.core.domain.inventory.entity.Inventory;
import com.ecommerce.core.domain.inventory.entity.InventoryMovement;
import com.ecommerce.core.domain.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InventoryRepositoryImpl implements InventoryRepository {
    
    private final InventoryJpaRepository inventoryJpaRepository;
    private final InventoryMovementJpaRepository movementJpaRepository;
    private final InventoryMapper mapper;
    
    @Override
    public Optional<Inventory> findByProductId(Long productId) {
        return inventoryJpaRepository.findByProductId(productId).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Inventory> findByProductIdAndVendorId(Long productId, Long vendorId) {
        return inventoryJpaRepository.findByProductIdAndVendorId(productId, vendorId).map(mapper::toDomain);
    }
    
    @Override
    public List<Inventory> findLowStockItems() {
        return inventoryJpaRepository.findLowStockItems().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<Inventory> findOutOfStockItems() {
        return inventoryJpaRepository.findOutOfStockItems().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = mapper.toEntity(inventory);
        InventoryEntity saved = inventoryJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public void delete(Long id) {
        inventoryJpaRepository.deleteById(id);
    }
    
    @Override
    public InventoryMovement saveMovement(InventoryMovement movement) {
        InventoryMovementEntity entity = mapper.toEntity(movement);
        InventoryMovementEntity saved = movementJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public List<InventoryMovement> findMovementsByProductId(Long productId) {
        return movementJpaRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    @Transactional
    public boolean reserveStock(Long productId, int quantity) {
        Optional<InventoryEntity> inventoryOpt = inventoryJpaRepository.findByProductId(productId);
        if (inventoryOpt.isEmpty()) return false;
        
        InventoryEntity inventory = inventoryOpt.get();
        if (inventory.getQuantity() - inventory.getReservedQuantity() < quantity) {
            return false;
        }
        
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryJpaRepository.save(inventory);
        return true;
    }
    
    @Override
    @Transactional
    public boolean releaseStock(Long productId, int quantity) {
        Optional<InventoryEntity> inventoryOpt = inventoryJpaRepository.findByProductId(productId);
        if (inventoryOpt.isEmpty()) return false;
        
        InventoryEntity inventory = inventoryOpt.get();
        inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
        inventoryJpaRepository.save(inventory);
        return true;
    }
    
    @Override
    @Transactional
    public boolean adjustStock(Long productId, int newQuantity, String reason) {
        Optional<InventoryEntity> inventoryOpt = inventoryJpaRepository.findByProductId(productId);
        if (inventoryOpt.isEmpty()) return false;
        
        InventoryEntity inventory = inventoryOpt.get();
        inventory.setQuantity(newQuantity);
        inventoryJpaRepository.save(inventory);
        return true;
    }
    
}