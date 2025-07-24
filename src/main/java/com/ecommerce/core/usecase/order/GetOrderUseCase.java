package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public GetOrderResponse execute(GetOrderRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new NotFoundException("Order not found"));

        // Verify order belongs to user (security check)
        if (!order.getUserId().equals(request.getUserId())) {
            throw new BusinessException("Order does not belong to user");
        }

        return mapToResponse(order);
    }

    public Page<GetOrderResponse> getUserOrders(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(this::mapToResponse);
    }

    public Page<GetOrderResponse> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::mapToResponse);
    }

    private GetOrderResponse mapToResponse(Order order) {
        return new GetOrderResponse(
            order.getId(),
            order.getOrderNumber(),
            order.getUserId(),
            order.getStatus().name(),
            order.getSubtotalAmount(),
            order.getTaxAmount(),
            order.getShippingAmount(),
            order.getDiscountAmount(),
            order.getTotalAmount(),
            order.getCurrency(),
            order.getItems(),
            order.getShippingAddress(),
            order.getBillingAddress(),
            order.getTrackingNumber(),
            order.getEstimatedDelivery(),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );
    }
}