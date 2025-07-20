package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    public CreateProductResponse execute(CreateProductRequest request) {
        // Validate input
        validateRequest(request);

        // Check business rules
        SKU sku = new SKU(request.getSku());
        if (productRepository.existsBySku(sku)) {
            throw new BusinessException("Product with SKU " + sku.getValue() + " already exists");
        }

        if (productRepository.existsBySlug(request.getSlug())) {
            throw new BusinessException("Product with slug " + request.getSlug() + " already exists");
        }

        // Create domain entity
        Price basePrice = new Price(request.getBasePrice(), request.getCurrency());
        Product product = new Product(
                request.getName(),
                request.getSlug(),
                request.getDescription(),
                sku,
                basePrice
        );

        // Set optional fields
        if (request.getSalePrice() != null) {
            Price salePrice = new Price(request.getSalePrice(), request.getCurrency());
            product.updateSalePrice(salePrice);
        }

        // Save product
        Product savedProduct = productRepository.save(product);

        // Return response
        return new CreateProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getSlug(),
                savedProduct.getSku().getValue(),
                savedProduct.getBasePrice().getAmount(),
                savedProduct.getBasePrice().getCurrency(),
                savedProduct.isActive(),
                savedProduct.getCreatedAt()
        );
    }

    private void validateRequest(CreateProductRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException("Product name is required");
        }
        if (request.getSlug() == null || request.getSlug().trim().isEmpty()) {
            throw new BusinessException("Product slug is required");
        }
        if (request.getSku() == null || request.getSku().trim().isEmpty()) {
            throw new BusinessException("Product SKU is required");
        }
        if (request.getBasePrice() == null) {
            throw new BusinessException("Base price is required");
        }
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new BusinessException("Currency is required");
        }
    }

    // Request and Response classes
    public static class CreateProductRequest {
        private String name;
        private String slug;
        private String description;
        private String sku;
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private String currency;

        // Constructors, getters, and setters
        public CreateProductRequest() {}

        public CreateProductRequest(String name, String slug, String description,
                                    String sku, BigDecimal basePrice, String currency) {
            this.name = name;
            this.slug = slug;
            this.description = description;
            this.sku = sku;
            this.basePrice = basePrice;
            this.currency = currency;
        }

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public BigDecimal getBasePrice() { return basePrice; }
        public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

        public BigDecimal getSalePrice() { return salePrice; }
        public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }
    }

    public static class CreateProductResponse {
        private final Long id;
        private final String name;
        private final String slug;
        private final String sku;
        private final BigDecimal basePrice;
        private final String currency;
        private final boolean isActive;
        private final java.time.LocalDateTime createdAt;

        public CreateProductResponse(Long id, String name, String slug, String sku,
                                     BigDecimal basePrice, String currency, boolean isActive,
                                     java.time.LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.slug = slug;
            this.sku = sku;
            this.basePrice = basePrice;
            this.currency = currency;
            this.isActive = isActive;
            this.createdAt = createdAt;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getSlug() { return slug; }
        public String getSku() { return sku; }
        public BigDecimal getBasePrice() { return basePrice; }
        public String getCurrency() { return currency; }
        public boolean isActive() { return isActive; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    }
}