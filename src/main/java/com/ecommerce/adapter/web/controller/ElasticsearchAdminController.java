package com.ecommerce.adapter.web.controller;

import com.ecommerce.infrastructure.service.ProductSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Admin controller for managing Elasticsearch operations
 */
@RestController
@RequestMapping("/api/v1/admin/elasticsearch")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchAdminController {

    private final ProductSyncService productSyncService;

    /**
     * Check Elasticsearch health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        boolean isHealthy = productSyncService.isElasticsearchHealthy();

        Map<String, Object> response = Map.of(
            "status", isHealthy ? "UP" : "DOWN",
            "service", "Elasticsearch",
            "timestamp", System.currentTimeMillis()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Manually sync all products to Elasticsearch
     */
    @PostMapping("/sync/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> syncAllProducts() {
        try {
            log.info("Manual sync all products request received");
            productSyncService.syncAllProductsToElasticsearch();

            return ResponseEntity.ok(Map.of(
                "message", "Product sync initiated successfully",
                "status", "success"
            ));
        } catch (Exception e) {
            log.error("Failed to sync products: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Failed to sync products: " + e.getMessage(),
                "status", "error"
            ));
        }
    }

    /**
     * Manually sync only active products to Elasticsearch
     */
    @PostMapping("/sync/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> syncActiveProducts() {
        try {
            log.info("Manual sync active products request received");
            productSyncService.syncActiveProductsToElasticsearch();

            return ResponseEntity.ok(Map.of(
                "message", "Active product sync initiated successfully",
                "status", "success"
            ));
        } catch (Exception e) {
            log.error("Failed to sync active products: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Failed to sync active products: " + e.getMessage(),
                "status", "error"
            ));
        }
    }

    /**
     * Sync a specific product by ID to Elasticsearch
     */
    @PostMapping("/sync/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> syncProduct(@PathVariable Long productId) {
        try {
            log.info("Manual sync product {} request received", productId);
            productSyncService.syncProductByIdToElasticsearch(productId);

            return ResponseEntity.ok(Map.of(
                "message", "Product " + productId + " synced successfully",
                "status", "success"
            ));
        } catch (Exception e) {
            log.error("Failed to sync product {}: {}", productId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Failed to sync product " + productId + ": " + e.getMessage(),
                "status", "error"
            ));
        }
    }

    /**
     * Remove a product from Elasticsearch index
     */
    @DeleteMapping("/product/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> removeProductFromIndex(@PathVariable String productId) {
        try {
            log.info("Manual remove product {} from index request received", productId);
            productSyncService.removeProductFromElasticsearch(productId);

            return ResponseEntity.ok(Map.of(
                "message", "Product " + productId + " removed from index successfully",
                "status", "success"
            ));
        } catch (Exception e) {
            log.error("Failed to remove product {} from index: {}", productId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Failed to remove product " + productId + " from index: " + e.getMessage(),
                "status", "error"
            ));
        }
    }

    /**
     * Reindex all products (clear and sync all)
     */
    @PostMapping("/reindex")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> reindexAllProducts() {
        try {
            log.info("Manual reindex all products request received");
            productSyncService.reindexAllProducts();

            return ResponseEntity.ok(Map.of(
                "message", "Product reindex initiated successfully",
                "status", "success"
            ));
        } catch (Exception e) {
            log.error("Failed to reindex products: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "message", "Failed to reindex products: " + e.getMessage(),
                "status", "error"
            ));
        }
    }
}
