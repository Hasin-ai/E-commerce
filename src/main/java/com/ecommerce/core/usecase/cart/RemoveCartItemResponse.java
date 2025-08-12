package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCartItemResponse {
    private Cart cart;
    private String message;
    private boolean success;

    public static RemoveCartItemResponse success(Cart cart) {
        return RemoveCartItemResponse.builder()
                .cart(cart)
                .message("Item removed from cart successfully")
                .success(true)
                .build();
    }

    public static RemoveCartItemResponse failure(String message) {
        return RemoveCartItemResponse.builder()
                .message(message)
                .success(false)
                .build();
    }
}