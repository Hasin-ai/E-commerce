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
public class UpdateCartItemResponse {
    private Cart cart;
    private String message;
    private boolean success;

    public static UpdateCartItemResponse success(Cart cart) {
        return UpdateCartItemResponse.builder()
                .cart(cart)
                .message("Cart item updated successfully")
                .success(true)
                .build();
    }

    public static UpdateCartItemResponse failure(String message) {
        return UpdateCartItemResponse.builder()
                .message(message)
                .success(false)
                .build();
    }
}
