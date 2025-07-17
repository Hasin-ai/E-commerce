package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.response.CartItemResponseDto;
import com.ecommerce.adapter.web.dto.response.CartResponseDto;
import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "id", target = "cartId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "totalPrice", expression = "java(calculateTotalPrice(cart.getItems()))")
    CartResponseDto toDto(Cart cart);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    CartItemResponseDto toDto(CartItem cartItem);

    List<CartItemResponseDto> toDto(List<CartItem> items);

    default BigDecimal calculateTotalPrice(List<CartItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}