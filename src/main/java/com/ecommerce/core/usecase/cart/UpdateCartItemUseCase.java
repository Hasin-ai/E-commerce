package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.shared.exception.NotFoundException;
import com.ecommerce.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCartItemUseCase {

    private final CartRepository cartRepository;

    @Transactional
    public UpdateCartItemResponse execute(UpdateCartItemRequest request) {
        try {
            Cart cart = cartRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new NotFoundException("Cart not found for user"));

            // Find the cart item to update
            CartItem itemToUpdate = cart.getItems().stream()
                    .filter(item -> item.getId().equals(request.getCartItemId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Cart item not found"));

            // Update the quantity
            itemToUpdate.updateQuantity(request.getQuantity());

            // Recalculate cart totals
            cart.recalculateTotal();

            Cart updatedCart = cartRepository.save(cart);
            return UpdateCartItemResponse.success(updatedCart);

        } catch (ValidationException | NotFoundException e) {
            return UpdateCartItemResponse.failure(e.getMessage());
        } catch (Exception e) {
            return UpdateCartItemResponse.failure("Failed to update cart item: " + e.getMessage());
        }
    }
}
