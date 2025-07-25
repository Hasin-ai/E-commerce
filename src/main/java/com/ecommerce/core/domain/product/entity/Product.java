package com.ecommerce.core.domain.product.entity;

import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

public class Product {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private Price price;
    private Long categoryId;
    private int stockQuantity;
    private boolean isActive;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String brand;
    private String slug;
    private boolean featured;

    // Default constructor for persistence layer
    public Product() {
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Product(String name, String description, String sku, Price price, Long categoryId, int stockQuantity) {
        validateName(name);
        validateDescription(description);
        validateSku(sku);
        validateStockQuantity(stockQuantity);

        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStock(int quantity) {
        if (quantity < 0) {
            throw new ValidationException("Stock quantity cannot be negative");
        }
        this.stockQuantity = quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(Price newPrice) {
        this.price = newPrice;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be positive");
        }
        if (stockQuantity < quantity) {
            throw new ValidationException("Insufficient stock");
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    public int getStock() {
        return this.stockQuantity;
    }

    public void reserveStock(int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be positive");
        }
        if (stockQuantity < quantity) {
            throw new ValidationException("Insufficient stock to reserve");
        }
        this.stockQuantity -= quantity;
        this.updatedAt = LocalDateTime.now();
    }

    // Validation methods
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Product name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new ValidationException("Product name too long. Maximum length is 255 characters");
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Product description cannot be null or empty");
        }
        if (description.length() > 1000) {
            throw new ValidationException("Product description too long. Maximum length is 1000 characters");
        }
    }

    private void validateSku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new ValidationException("Product SKU cannot be null or empty");
        }
        if (sku.length() > 50) {
            throw new ValidationException("Product SKU too long. Maximum length is 50 characters");
        }
        if (!sku.matches("^[A-Z0-9-_]+$")) {
            throw new ValidationException("Product SKU can only contain uppercase letters, numbers, hyphens, and underscores");
        }
    }

    private void validateStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new ValidationException("Stock quantity cannot be negative");
        }
    }

    public Price getCurrentPrice() {
        return this.price;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSku() { return sku; }
    public Price getPrice() { return price; }
    public Long getCategoryId() {
        return this.categoryId;
    }
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActive() { return isActive; }
    public List<String> getImageUrls() { return imageUrls; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getSlug() { return slug; }
    public boolean isFeatured() { return featured; }

    // Package-private setters for persistence
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setSku(String sku) { this.sku = sku; }
    public void setPrice(Price price) { this.price = price; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    
    // Additional setters for compatibility
    public void setPrice(java.math.BigDecimal price) { 
        this.price = new Price(price, "USD"); 
    }
    public void setCategoryId(long categoryId) { 
        this.categoryId = categoryId; 
    }
    public void setImageUrl(String imageUrl) { 
        this.imageUrls = List.of(imageUrl); 
    }
    public void setCurrency(String currency) { 
        // For compatibility - could store in Price object
    }
}
