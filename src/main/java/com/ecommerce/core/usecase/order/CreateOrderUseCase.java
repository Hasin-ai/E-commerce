package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.entity.OrderItem;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CreateOrderResponse execute(CreateOrderRequest request) {
        // Validate input
        validateRequest(request);

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + request.getUserId()));

        // Validate and create order items
        List<OrderItem> orderItems = request.getItems().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());

        // Create order
        Order order = new Order(request.getUserId(), orderItems);

        // Save order
        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getItems(),
                savedOrder.getTotal(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt()
        );
    }

    private void validateRequest(CreateOrderRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getUserId() == null) {
            throw new BusinessException("User ID is required");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException("Order must have at least one item");
        }
    }

    private OrderItem createOrderItem(CreateOrderRequest.OrderItemRequest itemRequest) {
        // Validate product exists and get current price
        Product product = productRepository.findById(itemRequest.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + itemRequest.getProductId()));

        // Check product availability
        if (!product.isAvailable()) {
            throw new BusinessException("Product is not available: " + product.getName());
        }

        // Check stock availability
        if (product.getStock() < itemRequest.getQuantity()) {
            throw new BusinessException("Insufficient stock for product: " + product.getName());
        }

        return new OrderItem(
                itemRequest.getProductId(),
                itemRequest.getQuantity(),
                product.getCurrentPrice()
        );
    }
}
