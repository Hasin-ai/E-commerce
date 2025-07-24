package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.order.entity.Address;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public CreateOrderUseCase(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public CreateOrderResponse execute(CreateOrderRequest request) {
        // Get user's cart
        Cart cart = cartRepository.findByUserId(request.getUserId())
            .orElseThrow(() -> new NotFoundException("Cart not found"));

        if (cart.isEmpty()) {
            throw new BusinessException("Cannot create order from empty cart");
        }

        // Create addresses
        Address shippingAddress = new Address(
            request.getShippingAddress().getStreet(),
            request.getShippingAddress().getCity(),
            request.getShippingAddress().getState(),
            request.getShippingAddress().getZipCode(),
            request.getShippingAddress().getCountry()
        );

        Address billingAddress = new Address(
            request.getBillingAddress().getStreet(),
            request.getBillingAddress().getCity(),
            request.getBillingAddress().getState(),
            request.getBillingAddress().getZipCode(),
            request.getBillingAddress().getCountry()
        );

        // Generate order number
        String orderNumber = generateOrderNumber();

        // Create order
        Order order = new Order(orderNumber, request.getUserId(), cart, shippingAddress, billingAddress);

        // Save order
        order = orderRepository.save(order);

        return new CreateOrderResponse(
            order.getId(),
            order.getOrderNumber(),
            order.getStatus().name(),
            order.getTotalAmount(),
            order.getCurrency(),
            "Order created successfully"
        );
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}