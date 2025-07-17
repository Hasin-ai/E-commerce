package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.shared.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GetProductUseCase {

    private final ProductRepository productRepository;

    public GetProductUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public GetProductResponse execute(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        return toGetProductResponse(product);
    }

    public GetProductResponse executeBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));

        return toGetProductResponse(product);
    }

    private GetProductResponse toGetProductResponse(Product product) {
        return new GetProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getSku().getValue(),
                product.getBasePrice().getAmount(),
                product.getSalePrice() != null ? product.getSalePrice().getAmount() : null,
                product.getBasePrice().getCurrency(),
                product.isActive(),
                product.isFeatured(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    // Response class
    public static class GetProductResponse {
        private final Long id;
        private final String name;
        private final String slug;
        private final String description;
        private final String sku;
        private final java.math.BigDecimal basePrice;
        private final java.math.BigDecimal salePrice;
        private final String currency;
        private final boolean isActive;
        private final boolean isFeatured;
        private final java.time.LocalDateTime createdAt;
        private final java.time.LocalDateTime updatedAt;

        public GetProductResponse(Long id, String name, String slug, String description,
                                  String sku, java.math.BigDecimal basePrice, java.math.BigDecimal salePrice,
                                  String currency, boolean isActive, boolean isFeatured,
                                  java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.slug = slug;
            this.description = description;
            this.sku = sku;
            this.basePrice = basePrice;
            this.salePrice = salePrice;
            this.currency = currency;
            this.isActive = isActive;
            this.isFeatured = isFeatured;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getSlug() { return slug; }
        public String getDescription() { return description; }
        public String getSku() { return sku; }
        public java.math.BigDecimal getBasePrice() { return basePrice; }
        public java.math.BigDecimal getSalePrice() { return salePrice; }
        public String getCurrency() { return currency; }
        public boolean isActive() { return isActive; }
        public boolean isFeatured() { return isFeatured; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    }
}
