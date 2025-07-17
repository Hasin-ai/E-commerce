package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.entity.CartJpaEntity;
import com.ecommerce.adapter.persistence.jpa.repository.CartJpaRepository;
import com.ecommerce.adapter.persistence.mapper.CartEntityMapper;
import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository cartJpaRepository;
    private final CartEntityMapper cartEntityMapper;

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartJpaRepository.findByUserId(userId).map(cartEntityMapper::toDomain);
    }

    @Override
    public Cart save(Cart cart) {
        CartJpaEntity cartJpaEntity = cartEntityMapper.toJpaEntity(cart);
        return cartEntityMapper.toDomain(cartJpaRepository.save(cartJpaEntity));
    }
}