package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.jpa.entity.CartJpaEntity;
import com.ecommerce.core.domain.cart.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserEntityMapper.class, CartItemEntityMapper.class})
public interface CartEntityMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "items", target = "items")
    Cart toDomain(CartJpaEntity entity);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "items", target = "items")
    CartJpaEntity toJpaEntity(Cart domain);
}