package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public CancelOrderResponse execute(CancelOrderRequest request) {
        validateRequest(request);

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + request.getOrderId()));

        // Validate user can cancel this order
        if (request.getUserId() != null && !order.getUserId().equals(request.getUserId())) {
            throw new BusinessException("Access denied: User can only cancel their own orders");
        }

        // Cancel the order (this will validate if cancellation is allowed)
        order.cancel();

        // Restore stock for all items if order was confirmed
        if (order.getStatus() == com.ecommerce.core.domain.order.valueobject.OrderStatus.CONFIRMED) {
            order.getItems().forEach(item -> {
                Product product = productRepository.findById(item.getProductId())
                        .orElse(null); // Product might have been deleted

                if (product != null) {
                    product.restoreStock(item.getQuantity());
                    productRepository.save(product);
                }
            });
        }

        Order savedOrder = orderRepository.save(order);

        return new CancelOrderResponse(
                savedOrder.getId(),
                savedOrder.getStatus(),
                savedOrder.getUpdatedAt(),
                request.getReason(),
                "Order has been successfully cancelled"
        );
    }

    private void validateRequest(CancelOrderRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getOrderId() == null) {
            throw new BusinessException("Order ID is required");
        }
    }
}
