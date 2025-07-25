package com.ecommerce.infrastructure.service;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.infrastructure.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Service for publishing product-related events
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publish product created event
     */
    public void publishProductCreated(Product product) {
        log.debug("Publishing product created event for product ID: {}", product.getId());
        eventPublisher.publishEvent(new ProductCreatedEvent(product));
    }

    /**
     * Publish product updated event
     */
    public void publishProductUpdated(Product product) {
        log.debug("Publishing product updated event for product ID: {}", product.getId());
        eventPublisher.publishEvent(new ProductUpdatedEvent(product));
    }

    /**
     * Publish product deleted event
     */
    public void publishProductDeleted(Long productId) {
        log.debug("Publishing product deleted event for product ID: {}", productId);
        eventPublisher.publishEvent(new ProductDeletedEvent(productId));
    }

    /**
     * Publish product stock updated event
     */
    public void publishProductStockUpdated(Product product, int previousStock, int newStock) {
        log.debug("Publishing product stock updated event for product ID: {}", product.getId());
        eventPublisher.publishEvent(new ProductStockUpdatedEvent(product, previousStock, newStock));
    }

    /**
     * Publish product status changed event
     */
    public void publishProductStatusChanged(Product product, boolean previousStatus, boolean newStatus) {
        log.debug("Publishing product status changed event for product ID: {}", product.getId());
        eventPublisher.publishEvent(new ProductStatusChangedEvent(product, previousStatus, newStatus));
    }
}
