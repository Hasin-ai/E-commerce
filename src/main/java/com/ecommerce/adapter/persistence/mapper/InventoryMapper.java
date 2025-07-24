package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.entity.InventoryEntity;
import com.ecommerce.adapter.persistence.entity.InventoryMovementEntity;
import com.ecommerce.core.domain.inventory.entity.Inventory;
import com.ecommerce.core.domain.inventory.entity.InventoryMovement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    
    Inventory toDomain(InventoryEntity entity);
    InventoryEntity toEntity(Inventory inventory);
    
    InventoryMovement toDomain(InventoryMovementEntity entity);
    InventoryMovementEntity toEntity(InventoryMovement movement);
}