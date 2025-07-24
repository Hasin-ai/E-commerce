package com.ecommerce.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CartResponseDto {

    private Long id;

    @JsonProperty("userId")
    private Long userId;

    private List<CartItemDto> items;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("totalItems")
    private Integer totalItems;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    // Constructors
    public CartResponseDto() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Integer getTotalItems() { return totalItems; }
    public void setTotalItems(Integer totalItems) { this.totalItems = totalItems; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class CartItemDto {
        private Long id;

        @JsonProperty("productId")
        private Long productId;

        @JsonProperty("productName")
        private String productName;

        @JsonProperty("productSku")
        private String productSku;

        private Integer quantity;

        @JsonProperty("unitPrice")
        private BigDecimal unitPrice;

        @JsonProperty("totalPrice")
        private BigDecimal totalPrice;

        // Constructors
        public CartItemDto() {}

        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getProductSku() { return productSku; }
        public void setProductSku(String productSku) { this.productSku = productSku; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    }
}