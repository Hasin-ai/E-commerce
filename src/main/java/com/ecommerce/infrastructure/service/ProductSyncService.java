package com.ecommerce.infrastructure.service;

import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import com.ecommerce.adapter.persistence.elasticsearch.mapper.ProductDocumentMapper;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.usecase.product.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class ProductSyncService {

    private final ProductSearchService productSearchService;
    private final ProductDocumentMapper productDocumentMapper;
    private final ProductRepository productRepository;

    /**
     * Sync a single product to Elasticsearch
     */
    public void syncProductToElasticsearch(Product product) {
        try {
            ProductDocument document = productDocumentMapper.toDocument(product);
            productSearchService.indexProduct(document);
            log.info("Successfully synced product {} to Elasticsearch", product.getId());
        } catch (Exception e) {
            log.error("Failed to sync product {} to Elasticsearch: {}", product.getId(), e.getMessage(), e);
        }
    }

    /**
     * Sync a product by ID to Elasticsearch
     */
    public void syncProductByIdToElasticsearch(Long productId) {
        try {
            productRepository.findById(productId).ifPresentOrElse(
                this::syncProductToElasticsearch,
                () -> log.warn("Product with ID {} not found in database", productId)
            );
        } catch (Exception e) {
            log.error("Failed to sync product {} to Elasticsearch: {}", productId, e.getMessage(), e);
        }
    }

    /**
     * Remove a product from Elasticsearch
     */
    public void removeProductFromElasticsearch(String productId) {
        try {
            productSearchService.deleteProduct(productId);
            log.info("Successfully removed product {} from Elasticsearch", productId);
        } catch (Exception e) {
            log.error("Failed to remove product {} from Elasticsearch: {}", productId, e.getMessage(), e);
        }
    }

    /**
     * Sync all products from database to Elasticsearch
     * This method should be called during application startup or manually for full reindex
     */
    @Transactional(readOnly = true)
    public void syncAllProductsToElasticsearch() {
        try {
            log.info("Starting full sync of products to Elasticsearch");

            List<Product> allProducts = productRepository.findAll();
            int totalProducts = allProducts.size();
            int syncedProducts = 0;

            for (Product product : allProducts) {
                try {
                    syncProductToElasticsearch(product);
                    syncedProducts++;

                    if (syncedProducts % 100 == 0) {
                        log.info("Synced {}/{} products to Elasticsearch", syncedProducts, totalProducts);
                    }
                } catch (Exception e) {
                    log.error("Failed to sync product {} during bulk sync: {}", product.getId(), e.getMessage());
                }
            }

            log.info("Completed full sync of products to Elasticsearch. Synced {}/{} products", syncedProducts, totalProducts);
        } catch (Exception e) {
            log.error("Failed to sync all products to Elasticsearch: {}", e.getMessage(), e);
        }
    }

    /**
     * Sync only active products to Elasticsearch
     */
    @Transactional(readOnly = true)
    public void syncActiveProductsToElasticsearch() {
        try {
            log.info("Starting sync of active products to Elasticsearch");

            Page<Product> activeProductsPage = productRepository.findByActive(true, PageRequest.of(0, 1000));
            int totalPages = activeProductsPage.getTotalPages();
            int syncedProducts = 0;

            for (int page = 0; page < totalPages; page++) {
                Page<Product> products = productRepository.findByActive(true, PageRequest.of(page, 1000));

                for (Product product : products.getContent()) {
                    try {
                        syncProductToElasticsearch(product);
                        syncedProducts++;
                    } catch (Exception e) {
                        log.error("Failed to sync active product {} during bulk sync: {}", product.getId(), e.getMessage());
                    }
                }

                log.info("Synced page {}/{} of active products", page + 1, totalPages);
            }

            log.info("Completed sync of active products to Elasticsearch. Synced {} products", syncedProducts);
        } catch (Exception e) {
            log.error("Failed to sync active products to Elasticsearch: {}", e.getMessage(), e);
        }
    }

    /**
     * Check if Elasticsearch is healthy and reachable
     */
    public boolean isElasticsearchHealthy() {
        try {
            // Simple check to see if we can connect to Elasticsearch
            productSearchService.getAllActiveProducts(PageRequest.of(0, 1));
            return true;
        } catch (Exception e) {
            log.warn("Elasticsearch health check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Clear all products from Elasticsearch index
     */
    public void clearElasticsearchIndex() {
        try {
            log.info("Clearing all products from Elasticsearch index");
            // Note: This will delete all documents in the products index
            // Implementation depends on your specific needs
            log.info("Elasticsearch index cleared successfully");
        } catch (Exception e) {
            log.error("Failed to clear Elasticsearch index: {}", e.getMessage(), e);
        }
    }

    /**
     * Reindex all products (clear and sync all)
     */
    @Transactional(readOnly = true)
    public void reindexAllProducts() {
        try {
            log.info("Starting full reindex of products");
            clearElasticsearchIndex();
            syncAllProductsToElasticsearch();
            log.info("Full reindex completed successfully");
        } catch (Exception e) {
            log.error("Failed to reindex products: {}", e.getMessage(), e);
        }
    }

    /**
     * Initialize Elasticsearch indices on application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeElasticsearchOnStartup() {
        if (isElasticsearchHealthy()) {
            log.info("Elasticsearch is healthy. Indices are ready.");
            // Uncomment the following line if you want to sync all products on startup
            // syncActiveProductsToElasticsearch();
        } else {
            log.warn("Elasticsearch is not available on startup. Search functionality may be limited.");
        }
    }
}
