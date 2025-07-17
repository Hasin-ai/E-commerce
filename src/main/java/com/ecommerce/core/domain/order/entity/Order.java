package com.ecommerce.core.domain.order.entity;

import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import com.ecommerce.core.domain.order.valueobject.Price;
import com.ecommerce.shared.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
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
}
