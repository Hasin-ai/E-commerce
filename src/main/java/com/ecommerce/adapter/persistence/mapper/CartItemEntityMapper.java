package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.jpa.entity.CartItemJpaEntity;
import com.ecommerce.core.domain.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class, SKUMapper.class, PriceMapper.class})
public interface CartItemEntityMapper {

    @Mapping(source = "product", target = "product")
    CartItem toDomain(CartItemJpaEntity entity);

    @Mapping(source = "product", target = "product")
    @Mapping(target = "cart", ignore = true)
    CartItemJpaEntity toJpaEntity(CartItem domain);
}