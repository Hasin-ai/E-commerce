package com.ecommerce.adapter.web.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDto {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}