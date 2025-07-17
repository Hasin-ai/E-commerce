package com.ecommerce.adapter.web.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponseDto {

    private Long cartId;
    private Long userId;
    private List<CartItemResponseDto> items;
    private BigDecimal totalPrice;
}