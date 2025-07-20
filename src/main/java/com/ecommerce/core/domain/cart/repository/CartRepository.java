package com.ecommerce.core.domain.cart.repository;

import com.ecommerce.core.domain.cart.entity.Cart;

import java.util.Optional;

public interface CartRepository {

    Optional<Cart> findByUserId(Long userId);

    Cart save(Cart cart);

    void deleteByUserId(Long userId);

}
