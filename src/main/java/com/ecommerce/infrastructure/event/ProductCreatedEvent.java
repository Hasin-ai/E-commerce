package com.ecommerce.infrastructure.event;

import com.ecommerce.core.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a product is created
 */
@Getter
@RequiredArgsConstructor
public class ProductCreatedEvent {
    private final Product product;
}
