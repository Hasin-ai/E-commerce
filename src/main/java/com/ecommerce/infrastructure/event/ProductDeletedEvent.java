package com.ecommerce.infrastructure.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a product is deleted
 */
@Getter
@RequiredArgsConstructor
public class ProductDeletedEvent {
    private final Long productId;
}
