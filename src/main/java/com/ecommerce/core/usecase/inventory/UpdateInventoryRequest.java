package com.ecommerce.core.usecase.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryRequest {
    private Long productId;
    private Long vendorId;
    private int quantity;
    private String reason;
    private Long userId;
}