package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepository productRepository;

    public void execute(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        validateRequest(request);

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getSlug() != null) {
            productRepository.findBySlug(request.getSlug()).ifPresent(p -> {
                if (!p.getId().equals(productId)) {
                    throw new BusinessException("Product with slug " + request.getSlug() + " already exists");
                }
            });
            product.setSlug(request.getSlug());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getSku() != null) {
            SKU sku = new SKU(request.getSku());
            productRepository.findBySku(sku).ifPresent(p -> {
                if (!p.getId().equals(productId)) {
                    throw new BusinessException("Product with SKU " + sku.getValue() + " already exists");
                }
            });
            product.setSku(sku);
        }
        if (request.getBasePrice() != null && request.getCurrency() != null) {
            product.updatePrice(new Price(request.getBasePrice(), request.getCurrency()));
        }
        if (request.getSalePrice() != null && request.getCurrency() != null) {
            product.updateSalePrice(new Price(request.getSalePrice(), request.getCurrency()));
        }
        if (request.getIsActive() != null) {
            if (request.getIsActive()) {
                product.activate();
            } else {
                product.deactivate();
            }
        }
        if (request.getIsFeatured() != null) {
            product.setFeatured(request.getIsFeatured());
        }


        productRepository.save(product);
    }

    private void validateRequest(UpdateProductRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
    }

    public static class UpdateProductRequest {
        private String name;
        private String slug;
        private String description;
        private String sku;
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private String currency;
        private Boolean isActive;
        private Boolean isFeatured;

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

        public Boolean getIsActive() { return isActive; }
        public void setIsActive(Boolean active) { isActive = active; }

        public Boolean getIsFeatured() { return isFeatured; }
        public void setIsFeatured(Boolean featured) { isFeatured = featured; }
    }
}