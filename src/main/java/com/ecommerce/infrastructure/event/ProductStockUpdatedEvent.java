package com.ecommerce.infrastructure.event;

import com.ecommerce.core.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a product's stock is updated
 */
@Getter
@RequiredArgsConstructor
public class ProductStockUpdatedEvent {
    private final Product product;
    private final int previousStock;
    private final int newStock;
}
