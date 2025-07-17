package com.ecommerce.core.domain.order.repository;

import com.ecommerce.core.domain.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findByUserId(Long userId);

    void deleteById(Long id);
}
