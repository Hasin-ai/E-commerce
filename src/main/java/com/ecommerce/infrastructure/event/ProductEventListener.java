package com.ecommerce.infrastructure.event;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.infrastructure.service.ProductSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Event listener for product changes to automatically sync with Elasticsearch
 */
@Component
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class ProductEventListener {

    private final ProductSyncService productSyncService;

    /**
     * Handle product creation events
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("Handling product created event for product ID: {}", event.getProduct().getId());
        productSyncService.syncProductToElasticsearch(event.getProduct());
    }

    /**
     * Handle product update events
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductUpdated(ProductUpdatedEvent event) {
        log.info("Handling product updated event for product ID: {}", event.getProduct().getId());
        productSyncService.syncProductToElasticsearch(event.getProduct());
    }

    /**
     * Handle product deletion events
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductDeleted(ProductDeletedEvent event) {
        log.info("Handling product deleted event for product ID: {}", event.getProductId());
        productSyncService.removeProductFromElasticsearch(event.getProductId().toString());
    }

    /**
     * Handle product stock update events
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductStockUpdated(ProductStockUpdatedEvent event) {
        log.info("Handling product stock updated event for product ID: {}", event.getProduct().getId());
        productSyncService.syncProductToElasticsearch(event.getProduct());
    }

    /**
     * Handle product activation/deactivation events
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductStatusChanged(ProductStatusChangedEvent event) {
        log.info("Handling product status changed event for product ID: {}", event.getProduct().getId());
        if (event.getProduct().isActive()) {
            productSyncService.syncProductToElasticsearch(event.getProduct());
        } else {
            productSyncService.removeProductFromElasticsearch(event.getProduct().getId().toString());
        }
    }
}
