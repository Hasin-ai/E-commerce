package com.ecommerce.core.domain.order.entity;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.shared.exception.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private String orderNumber;
    private Long userId;
    private OrderStatus status;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currency;
    private List<OrderItem> items;
    private Address shippingAddress;
    private Address billingAddress;
    private String trackingNumber;
    private LocalDateTime estimatedDelivery;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(String orderNumber, Long userId, Cart cart, Address shippingAddress, Address billingAddress) {
        validateOrderNumber(orderNumber);
        validateUserId(userId);
        validateCart(cart);
        
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.status = OrderStatus.PENDING;
        this.items = new ArrayList<>();
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.currency = "USD"; // Default currency
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // Convert cart items to order items
        cart.getItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem(
                cartItem.getProductId(),
                cartItem.getProductName(),
                null, // productSku - CartItem doesn't have SKU, can be enhanced later
                cartItem.getQuantity(),
                cartItem.getUnitPrice()
            );
            this.items.add(orderItem);
        });
        
        calculateAmounts();
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new ValidationException("Can only confirm pending orders");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void startProcessing() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new ValidationException("Can only process confirmed orders");
        }
        this.status = OrderStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    public void ship(String trackingNumber) {
        if (this.status != OrderStatus.PROCESSING) {
            throw new ValidationException("Can only ship processing orders");
        }
        this.status = OrderStatus.SHIPPED;
        this.trackingNumber = trackingNumber;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new ValidationException("Can only deliver shipped orders");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == OrderStatus.DELIVERED || this.status == OrderStatus.CANCELLED) {
            throw new ValidationException("Cannot cancel delivered or already cancelled orders");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    private void calculateAmounts() {
        this.subtotalAmount = items.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate tax (8% for example)
        this.taxAmount = subtotalAmount.multiply(new BigDecimal("0.08"));
        
        // Calculate shipping (free for orders over $50)
        this.shippingAmount = subtotalAmount.compareTo(new BigDecimal("50")) >= 0 
            ? BigDecimal.ZERO 
            : new BigDecimal("5.99");
        
        // Discount amount (default 0)
        this.discountAmount = this.discountAmount != null ? this.discountAmount : BigDecimal.ZERO;
        
        // Total amount
        this.totalAmount = subtotalAmount
            .add(taxAmount)
            .add(shippingAmount)
            .subtract(discountAmount);
    }

    public void applyDiscount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        calculateAmounts();
        this.updatedAt = LocalDateTime.now();
    }

    private void validateOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.trim().isEmpty()) {
            throw new ValidationException("Order number cannot be empty");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("User ID must be positive");
        }
    }

    private void validateCart(Cart cart) {
        if (cart == null || cart.isEmpty()) {
            throw new ValidationException("Cannot create order from empty cart");
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public Long getUserId() { return userId; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getSubtotalAmount() { return subtotalAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getShippingAmount() { return shippingAmount; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public Address getShippingAddress() { return shippingAddress; }
    public Address getBillingAddress() { return billingAddress; }
    public String getTrackingNumber() { return trackingNumber; }
    public LocalDateTime getEstimatedDelivery() { return estimatedDelivery; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }
    void setItems(List<OrderItem> items) { this.items = items; }
}