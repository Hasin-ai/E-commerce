package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.repository.OrderJpaRepository;
import com.ecommerce.adapter.persistence.mapper.OrderEntityMapper;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderEntityMapper orderEntityMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderEntityMapper = orderEntityMapper;
    }

    @Override
    public Order save(Order order) {
        return orderEntityMapper.toDomainEntity(orderJpaRepository.save(orderEntityMapper.toJpaEntity(order)));
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(orderEntityMapper::toDomainEntity);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderJpaRepository.findByUserId(userId).stream().map(orderEntityMapper::toDomainEntity).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        orderJpaRepository.deleteById(id);
    }
}
