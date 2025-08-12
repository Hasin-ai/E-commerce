package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClearCartUseCase {
    
    private final CartRepository cartRepository;

    public ClearCartResponse execute(ClearCartRequest request) {
        try {
            Cart cart = cartRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new NotFoundException("Cart not found for user"));

            cart.clear();
            cartRepository.save(cart);
            
            return ClearCartResponse.success();
            
        } catch (Exception e) {
            return ClearCartResponse.failure("Failed to clear cart: " + e.getMessage());
        }
    }
}