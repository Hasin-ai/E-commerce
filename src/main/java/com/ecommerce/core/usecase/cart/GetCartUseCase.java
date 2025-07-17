package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCartUseCase {

    private final CartRepository cartRepository;

    public Cart execute(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }
}