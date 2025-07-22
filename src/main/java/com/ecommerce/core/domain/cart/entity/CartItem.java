package com.ecommerce.core.domain.cart.entity;

import com.ecommerce.shared.exception.ValidationException;
import java.math.BigDecimal;

public class CartItem {
    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;

    public CartItem(Long productId, String productName, BigDecimal unitPrice, int quantity) {
        validateQuantity(quantity);
        validatePrice(unitPrice);

        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void updateQuantity(int newQuantity) {
        validateQuantity(newQuantity);
        this.quantity = newQuantity;
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void updatePrice(BigDecimal newPrice) {
        validatePrice(newPrice);
        this.unitPrice = newPrice;
        this.totalPrice = newPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be positive");
        }
        if (quantity > 999) {
            throw new ValidationException("Quantity cannot exceed 999");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be positive");
        }
    }

    public BigDecimal getPrice() {
        return this.unitPrice;
    }

    public void setQuantity(int quantity) {
        updateQuantity(quantity);
    }

    // Getters
    public Long getId() { return id; }
    public Long getCartId() { return cartId; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public BigDecimal getTotalPrice() { return totalPrice; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }
    void setCartId(Long cartId) { this.cartId = cartId; }
}
