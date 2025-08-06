package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveCartItemUseCase {
    
    private final CartRepository cartRepository;

    public RemoveCartItemResponse execute(RemoveCartItemRequest request) {
        try {
            Cart cart = cartRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new NotFoundException("Cart not found for user"));

            if (request.getProductId() != null) {
                cart.removeItem(request.getProductId());
            } else if (request.getCartItemId() != null) {
                // Find product ID by cart item ID and remove
                cart.getItems().stream()
                    .filter(item -> item.getId().equals(request.getCartItemId()))
                    .findFirst()
                    .ifPresentOrElse(
                        item -> cart.removeItem(item.getProductId()),
                        () -> { throw new NotFoundException("Cart item not found"); }
                    );
            } else {
                throw new IllegalArgumentException("Either productId or cartItemId must be provided");
            }

            Cart updatedCart = cartRepository.save(cart);
            return RemoveCartItemResponse.success(updatedCart);
            
        } catch (Exception e) {
            return RemoveCartItemResponse.failure("Failed to remove item from cart: " + e.getMessage());
        }
    }
}