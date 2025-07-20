package com.ecommerce.core.domain.order.entity;

import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;

public class OrderItem {

    private Long id;
    private Long productId;
    private Integer quantity;
    private Price unitPrice;
    private Price totalPrice;

    public OrderItem(Long productId, Integer quantity, Price unitPrice) {
        if (productId == null) {
            throw new BusinessException("Product ID cannot be null");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("Quantity must be positive");
        }
        if (unitPrice == null || unitPrice.isNegative()) {
            throw new BusinessException("Unit price must be a positive value");
        }

        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(quantity);
    }

    public void updateQuantity(int newQuantity) {
        if (newQuantity <= 0) {
            throw new BusinessException("Quantity must be positive");
        }
        this.quantity = newQuantity;
        this.totalPrice = this.unitPrice.multiply(newQuantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Price getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Price unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Price totalPrice) {
        this.totalPrice = totalPrice;
    }
}