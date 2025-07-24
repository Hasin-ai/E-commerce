package com.ecommerce.core.usecase.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GetProductResponse {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Long categoryId;
    private String imageUrl;
    private Integer stockQuantity;
    private boolean active;
    private boolean featured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GetProductResponse(Long id, String name, String description, String sku, BigDecimal price,
                            BigDecimal discountPrice, Long categoryId, String imageUrl, Integer stockQuantity,
                            boolean active, boolean featured, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.discountPrice = discountPrice;
        this.categoryId = categoryId;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.active = active;
        this.featured = featured;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSku() { return sku; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getDiscountPrice() { return discountPrice; }
    public Long getCategoryId() { return categoryId; }
    public String getImageUrl() { return imageUrl; }
    public Integer getStockQuantity() { return stockQuantity; }
    public boolean isActive() { return active; }
    public boolean isFeatured() { return featured; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}