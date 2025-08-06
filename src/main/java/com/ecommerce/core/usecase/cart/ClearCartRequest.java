package com.ecommerce.core.usecase.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClearCartRequest {
    private Long userId;

    public static ClearCartRequest forUser(Long userId) {
        return ClearCartRequest.builder()
                .userId(userId)
                .build();
    }
}