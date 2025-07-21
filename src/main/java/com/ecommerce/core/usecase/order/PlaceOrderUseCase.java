package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlaceOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public PlaceOrderResponse execute(PlaceOrderRequest request) {
        // Validate input
        validateRequest(request);

        // Find the order
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + request.getOrderId()));

        // Validate order can be placed
        if (order.getStatus() != com.ecommerce.core.domain.order.valueobject.OrderStatus.PENDING) {
            throw new BusinessException("Only pending orders can be placed");
        }

        // Reserve stock for all items
        order.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found with ID: " + item.getProductId()));

            // Check stock availability again
            if (product.getStock() < item.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getName());
            }

            // Reserve stock
            product.reserveStock(item.getQuantity());
            productRepository.save(product);
        });

        // Confirm the order
        order.confirm();

        // Save the updated order
        Order savedOrder = orderRepository.save(order);

        return new PlaceOrderResponse(
                savedOrder.getId(),
                savedOrder.getStatus(),
                savedOrder.getTotal(),
                savedOrder.getUpdatedAt(),
                "Order has been successfully placed and confirmed"
        );
    }

    private void validateRequest(PlaceOrderRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getOrderId() == null) {
            throw new BusinessException("Order ID is required");
        }
    }
}
