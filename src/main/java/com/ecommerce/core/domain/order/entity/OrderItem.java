package com.ecommerce.core.domain.order.entity;

import com.ecommerce.shared.exception.ValidationException;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public OrderItem(Long productId, String productName, String productSku, Integer quantity, BigDecimal unitPrice) {
        validateProductId(productId);
        validateProductName(productName);
        validateQuantity(quantity);
        validateUnitPrice(unitPrice);

        this.productId = productId;
        this.productName = productName;
        this.productSku = productSku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(new BigDecimal(quantity));
    }

    private void validateProductId(Long productId) {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Product ID must be positive");
        }
    }

    private void validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new ValidationException("Product name cannot be empty");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new ValidationException("Quantity must be positive");
        }
    }

    private void validateUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Unit price cannot be negative");
        }
    }

    // Getters
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getProductSku() { return productSku; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }
}