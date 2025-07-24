package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetCartUseCase {

    private final CartRepository cartRepository;

    public GetCartUseCase(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public GetCartResponse execute(GetCartRequest request) {
        Cart cart = cartRepository.findByUserId(request.getUserId())
            .orElse(new Cart(request.getUserId()));

        return new GetCartResponse(
            cart.getId(),
            cart.getUserId(),
            cart.getItems(),
            cart.getTotalAmount(),
            cart.getTotalItems(),
            cart.getCreatedAt(),
            cart.getUpdatedAt()
        );
    }
}