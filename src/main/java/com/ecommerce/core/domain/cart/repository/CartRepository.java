package com.ecommerce.core.domain.cart.repository;

import com.ecommerce.core.domain.cart.entity.Cart;
import java.util.Optional;

public interface CartRepository {
    Cart save(Cart cart);
    Optional<Cart> findByUserId(Long userId);
    void delete(Cart cart);
}
