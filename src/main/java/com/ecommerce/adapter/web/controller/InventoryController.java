package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.UpdateInventoryRequestDto;
import com.ecommerce.adapter.web.dto.response.InventoryResponseDto;
import com.ecommerce.core.usecase.inventory.UpdateInventoryRequest;
import com.ecommerce.core.usecase.inventory.UpdateInventoryUseCase;
import com.ecommerce.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final UpdateInventoryUseCase updateInventoryUseCase;
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryResponseDto>> getInventoryByProduct(@PathVariable Long productId) {
        // TODO: Implement GetInventoryByProductUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Inventory retrieved successfully"));
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
    public ResponseEntity<ApiResponse<List<InventoryResponseDto>>> getLowStockItems() {
        // TODO: Implement GetLowStockItemsUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Low stock items retrieved successfully"));
    }
    
    @GetMapping("/out-of-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
    public ResponseEntity<ApiResponse<List<InventoryResponseDto>>> getOutOfStockItems() {
        // TODO: Implement GetOutOfStockItemsUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Out of stock items retrieved successfully"));
    }
    
    @PutMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
    public ResponseEntity<ApiResponse<Void>> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateInventoryRequestDto request,
            Authentication authentication) {
        
        // TODO: Get user ID from authentication
        Long userId = 1L; // Placeholder
        
        UpdateInventoryRequest useCaseRequest = UpdateInventoryRequest.builder()
                .productId(productId)
                .vendorId(request.getVendorId())
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .userId(userId)
                .build();
        
        var response = updateInventoryUseCase.execute(useCaseRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(null, response.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage()));
        }
    }
    
    @PostMapping("/product/{productId}/reserve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
    public ResponseEntity<ApiResponse<Void>> reserveStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {
        
        // TODO: Implement ReserveStockUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Stock reserved successfully"));
    }
    
    @PostMapping("/product/{productId}/release")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VENDOR')")
    public ResponseEntity<ApiResponse<Void>> releaseStock(
            @PathVariable Long productId,
            @RequestParam int quantity) {
        
        // TODO: Implement ReleaseStockUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Stock released successfully"));
    }
}