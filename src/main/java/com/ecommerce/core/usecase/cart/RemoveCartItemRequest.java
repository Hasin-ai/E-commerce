package com.ecommerce.core.usecase.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCartItemRequest {
    private Long userId;
    private Long productId;
    private Long cartItemId;

    public static RemoveCartItemRequest byProductId(Long userId, Long productId) {
        return RemoveCartItemRequest.builder()
                .userId(userId)
                .productId(productId)
                .build();
    }

    public static RemoveCartItemRequest byCartItemId(Long userId, Long cartItemId) {
        return RemoveCartItemRequest.builder()
                .userId(userId)
                .cartItemId(cartItemId)
                .build();
    }
}