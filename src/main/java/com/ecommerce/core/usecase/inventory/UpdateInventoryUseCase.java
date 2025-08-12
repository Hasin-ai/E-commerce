package com.ecommerce.core.usecase.inventory;

import com.ecommerce.core.domain.inventory.entity.Inventory;
import com.ecommerce.core.domain.inventory.entity.InventoryMovement;
import com.ecommerce.core.domain.inventory.entity.MovementType;
import com.ecommerce.core.domain.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateInventoryUseCase {
    
    private final InventoryRepository inventoryRepository;
    
    @Transactional
    public UpdateInventoryResponse execute(UpdateInventoryRequest request) {
        try {
            var inventoryOpt = inventoryRepository.findByProductIdAndVendorId(
                    request.getProductId(), request.getVendorId());
            
            if (inventoryOpt.isEmpty()) {
                return UpdateInventoryResponse.builder()
                        .success(false)
                        .message("Inventory not found for product and vendor")
                        .build();
            }
            
            Inventory inventory = inventoryOpt.get();
            int previousQuantity = inventory.getQuantity();
            
            inventory.setQuantity(request.getQuantity());
            inventory.setLastUpdated(LocalDateTime.now());
            
            inventoryRepository.save(inventory);
            
            // Record movement
            InventoryMovement movement = InventoryMovement.builder()
                    .inventoryId(inventory.getId())
                    .productId(request.getProductId())
                    .type(MovementType.ADJUSTMENT)
                    .quantity(request.getQuantity() - previousQuantity)
                    .previousQuantity(previousQuantity)
                    .newQuantity(request.getQuantity())
                    .reason(request.getReason())
                    .userId(request.getUserId())
                    .createdAt(LocalDateTime.now())
                    .build();
            
            inventoryRepository.saveMovement(movement);
            
            return UpdateInventoryResponse.builder()
                    .success(true)
                    .message("Inventory updated successfully")
                    .previousQuantity(previousQuantity)
                    .newQuantity(request.getQuantity())
                    .build();
                    
        } catch (Exception e) {
            return UpdateInventoryResponse.builder()
                    .success(false)
                    .message("Failed to update inventory: " + e.getMessage())
                    .build();
        }
    }
}