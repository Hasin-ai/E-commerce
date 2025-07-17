package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.entity.OrderItem;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.order.valueobject.Price;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public PlaceOrderUseCase(OrderRepository orderRepository, ProductRepository productRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public PlaceOrderResponse execute(PlaceOrderRequest request) {
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user"));

        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Cannot place an order with an empty cart");
        }

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    Product product = productRepository.findById(cartItem.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
                    return new OrderItem(product.getId(), cartItem.getQuantity(), product.getBasePrice());
                })
                .collect(Collectors.toList());

        Order order = new Order(request.getUserId(), orderItems);
        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteByUserId(request.getUserId());

        return PlaceOrderResponse.builder()
                .orderId(savedOrder.getId())
                .build();
    }

    @Getter
    @Setter
    @Builder
    public static class PlaceOrderRequest {
        private Long userId;
    }

    @Getter
    @Setter
    @Builder
    public static class PlaceOrderResponse {
        private Long orderId;
    }
}
