package com.ecommerce.infrastructure.config;

import com.ecommerce.adapter.persistence.implementation.OrderRepositoryImpl;
import com.ecommerce.adapter.persistence.jpa.repository.OrderJpaRepository;
import com.ecommerce.adapter.persistence.mapper.OrderEntityMapper;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.usecase.order.PlaceOrderUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

    @Bean
    public OrderRepository orderRepository(OrderJpaRepository orderJpaRepository, OrderEntityMapper orderEntityMapper) {
        return new OrderRepositoryImpl(orderJpaRepository, orderEntityMapper);
    }

    @Bean
    public PlaceOrderUseCase placeOrderUseCase(OrderRepository orderRepository, ProductRepository productRepository, CartRepository cartRepository) {
        return new PlaceOrderUseCase(orderRepository, productRepository, cartRepository);
    }
}
