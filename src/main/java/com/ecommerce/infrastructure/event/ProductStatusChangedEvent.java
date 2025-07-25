package com.ecommerce.infrastructure.event;

import com.ecommerce.core.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a product's status (active/inactive) is changed
 */
@Getter
@RequiredArgsConstructor
public class ProductStatusChangedEvent {
    private final Product product;
    private final boolean previousStatus;
    private final boolean newStatus;
}
