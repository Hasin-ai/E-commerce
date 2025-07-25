package com.ecommerce.infrastructure.event;

import com.ecommerce.core.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a product is updated
 */
@Getter
@RequiredArgsConstructor
public class ProductUpdatedEvent {
    private final Product product;
}
