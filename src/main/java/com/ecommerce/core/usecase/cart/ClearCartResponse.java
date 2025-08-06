package com.ecommerce.core.usecase.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClearCartResponse {
    private String message;
    private boolean success;

    public static ClearCartResponse success() {
        return ClearCartResponse.builder()
                .message("Cart cleared successfully")
                .success(true)
                .build();
    }

    public static ClearCartResponse failure(String message) {
        return ClearCartResponse.builder()
                .message(message)
                .success(false)
                .build();
    }
}