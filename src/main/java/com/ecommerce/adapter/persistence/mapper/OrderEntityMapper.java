package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.jpa.entity.OrderJpaEntity;
import com.ecommerce.adapter.persistence.jpa.entity.OrderItemJpaEntity;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.entity.OrderItem;
import com.ecommerce.core.domain.order.valueobject.Price;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderEntityMapper {

    public OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity jpaEntity = new OrderJpaEntity();
        jpaEntity.setId(order.getId());
        jpaEntity.setUserId(order.getUserId());
        jpaEntity.setTotal(order.getTotal().getAmount());
        jpaEntity.setStatus(order.getStatus());
        jpaEntity.setCreatedAt(order.getCreatedAt());
        jpaEntity.setUpdatedAt(order.getUpdatedAt());
        jpaEntity.setItems(order.getItems().stream().map(item -> toJpaEntity(item, jpaEntity)).collect(Collectors.toList()));
        return jpaEntity;
    }

    public Order toDomainEntity(OrderJpaEntity jpaEntity) {
        Order order = new Order(jpaEntity.getUserId(), jpaEntity.getItems().stream().map(this::toDomainEntity).collect(Collectors.toList()));
        order.setId(jpaEntity.getId());
        order.setStatus(jpaEntity.getStatus());
        order.setCreatedAt(jpaEntity.getCreatedAt());
        order.setUpdatedAt(jpaEntity.getUpdatedAt());
        return order;
    }

    private OrderItemJpaEntity toJpaEntity(OrderItem orderItem, OrderJpaEntity orderJpaEntity) {
        OrderItemJpaEntity jpaEntity = new OrderItemJpaEntity();
        jpaEntity.setId(orderItem.getId());
        jpaEntity.setOrder(orderJpaEntity);
        jpaEntity.setProductId(orderItem.getProductId());
        jpaEntity.setQuantity(orderItem.getQuantity());
        jpaEntity.setUnitPrice(orderItem.getUnitPrice().getAmount());
        jpaEntity.setTotalPrice(orderItem.getTotalPrice().getAmount());
        return jpaEntity;
    }

    private OrderItem toDomainEntity(OrderItemJpaEntity jpaEntity) {
        OrderItem orderItem = new OrderItem(jpaEntity.getProductId(), jpaEntity.getQuantity(), new Price(jpaEntity.getUnitPrice(), "USD"));
        orderItem.setId(jpaEntity.getId());
        return orderItem;
    }
}
