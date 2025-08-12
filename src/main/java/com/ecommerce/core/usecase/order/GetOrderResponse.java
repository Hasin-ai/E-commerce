package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.OrderItem;
import com.ecommerce.core.domain.order.entity.Address;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GetOrderResponse {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String status;
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

    public GetOrderResponse(Long id, String orderNumber, Long userId, String status,
                          BigDecimal subtotalAmount, BigDecimal taxAmount, BigDecimal shippingAmount,
                          BigDecimal discountAmount, BigDecimal totalAmount, String currency,
                          List<OrderItem> items, Address shippingAddress, Address billingAddress,
                          String trackingNumber, LocalDateTime estimatedDelivery,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.status = status;
        this.subtotalAmount = subtotalAmount;
        this.taxAmount = taxAmount;
        this.shippingAmount = shippingAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.trackingNumber = trackingNumber;
        this.estimatedDelivery = estimatedDelivery;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getOrderNumber() { return orderNumber; }
    public Long getUserId() { return userId; }
    public String getStatus() { return status; }
    public BigDecimal getSubtotalAmount() { return subtotalAmount; }
    public BigDecimal getTaxAmount() { return taxAmount; }
    public BigDecimal getShippingAmount() { return shippingAmount; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public List<OrderItem> getItems() { return items; }
    public Address getShippingAddress() { return shippingAddress; }
    public Address getBillingAddress() { return billingAddress; }
    public String getTrackingNumber() { return trackingNumber; }
    public LocalDateTime getEstimatedDelivery() { return estimatedDelivery; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}