package com.ecommerce.core.usecase.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInventoryResponse {
    private boolean success;
    private String message;
    private int previousQuantity;
    private int newQuantity;
}