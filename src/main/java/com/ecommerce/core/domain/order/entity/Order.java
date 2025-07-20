package com.ecommerce.core.domain.order.entity;

import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private Price total;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order(Long userId, List<OrderItem> items) {
        if (userId == null) {
            throw new BusinessException("User ID cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new BusinessException("Order must have at least one item");
        }

        this.userId = userId;
        this.items = items;
        this.total = calculateTotal(items);
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    private Price calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(Price::add)
                .orElse(new Price("0.00", "USD"));
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessException("Only pending orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void ship() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new BusinessException("Only confirmed orders can be shipped");
        }
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new BusinessException("Only shipped orders can be delivered");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == OrderStatus.DELIVERED || this.status == OrderStatus.SHIPPED) {
            throw new BusinessException("Cannot cancel an order that has been shipped or delivered");
        }
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Price getTotal() {
        return total;
    }

    public void setTotal(Price total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}