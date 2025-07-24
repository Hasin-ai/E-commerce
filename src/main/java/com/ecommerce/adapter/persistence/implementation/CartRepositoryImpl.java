package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.adapter.persistence.jpa.CartJpaRepository;
import com.ecommerce.adapter.persistence.entity.CartEntity;
import com.ecommerce.adapter.persistence.mapper.CartMapper;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository jpaRepository;
    private final CartMapper mapper;

    public CartRepositoryImpl(CartJpaRepository jpaRepository, CartMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = mapper.toEntity(cart);
        CartEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
            .map(mapper::toDomain);
    }

    @Override
    public void delete(Cart cart) {
        CartEntity entity = mapper.toEntity(cart);
        jpaRepository.delete(entity);
    }
}