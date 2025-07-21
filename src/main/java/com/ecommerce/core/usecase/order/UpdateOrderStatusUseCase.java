package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;

    public UpdateOrderStatusResponse execute(UpdateOrderStatusRequest request) {
        validateRequest(request);

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + request.getOrderId()));

        // Update status based on target status
        switch (request.getNewStatus()) {
            case CONFIRMED:
                order.confirm();
                break;
            case SHIPPED:
                order.ship();
                break;
            case DELIVERED:
                order.deliver();
                break;
            case CANCELLED:
                order.cancel();
                break;
            default:
                throw new BusinessException("Invalid status transition to: " + request.getNewStatus());
        }

        Order savedOrder = orderRepository.save(order);

        return new UpdateOrderStatusResponse(
                savedOrder.getId(),
                savedOrder.getStatus(),
                savedOrder.getUpdatedAt(),
                "Order status updated successfully to " + savedOrder.getStatus()
        );
    }

    private void validateRequest(UpdateOrderStatusRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getOrderId() == null) {
            throw new BusinessException("Order ID is required");
        }
        if (request.getNewStatus() == null) {
            throw new BusinessException("New status is required");
        }
    }
}
