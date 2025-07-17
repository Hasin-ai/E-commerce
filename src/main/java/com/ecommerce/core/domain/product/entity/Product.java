package com.ecommerce.core.domain.product.entity;

import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.domain.product.valueobject.SKU;
import com.ecommerce.shared.exception.BusinessException;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private SKU sku;
    private Price basePrice;
    private Price salePrice;
    private boolean isActive;
    private boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public Product(String name, String slug, String description,
                   SKU sku, Price basePrice) {
        validateName(name);
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.sku = sku;
        this.basePrice = basePrice;
        this.isActive = true;
        this.isFeatured = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void updatePrice(Price newPrice) {
        if (newPrice.isNegative()) {
            throw new BusinessException("Price cannot be negative");
        }
        this.basePrice = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateSalePrice(Price newSalePrice) {
        if (newSalePrice != null && newSalePrice.isNegative()) {
            throw new BusinessException("Sale price cannot be negative");
        }
        this.salePrice = newSalePrice;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return isActive;
    }

    // Private validation methods
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("Product name cannot be empty");
        }
        if (name.length() > 255) {
            throw new BusinessException("Product name too long");
        }
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSlug() { return slug; }
    public String getDescription() { return description; }
    public SKU getSku() { return sku; }
    public Price getBasePrice() { return basePrice; }
    public Price getSalePrice() { return salePrice; }
    public boolean isActive() { return isActive; }
    public boolean isFeatured() { return isFeatured; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSku(SKU sku) {
        this.sku = sku;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    // Package-private setters for repository use
    void setId(Long id) { this.id = id; }
    void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
