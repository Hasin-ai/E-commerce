package com.ecommerce.core.usecase.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCartItemRequest {
    private Long userId;
    private Long cartItemId;
    private int quantity;

    public static UpdateCartItemRequest of(Long userId, Long cartItemId, int quantity) {
        return UpdateCartItemRequest.builder()
                .userId(userId)
                .cartItemId(cartItemId)
                .quantity(quantity)
                .build();
    }
}
