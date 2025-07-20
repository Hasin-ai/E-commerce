package com.ecommerce.core.domain.cart.entity;

import com.ecommerce.core.domain.product.entity.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItem {

    private Long id;
    private Product product;
    private Integer quantity;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getProductId() {
        return product.getId();
    }
}