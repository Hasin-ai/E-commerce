package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderResponse execute(GetOrderRequest request) {
        validateRequest(request);

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + request.getOrderId()));

        // Validate user has access to this order
        if (request.getUserId() != null && !order.getUserId().equals(request.getUserId())) {
            throw new BusinessException("Access denied: User can only view their own orders");
        }

        return new GetOrderResponse(
                order.getId(),
                order.getUserId(),
                order.getItems(),
                order.getTotal(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public GetOrderListResponse executeGetUserOrders(GetUserOrdersRequest request) {
        validateUserOrdersRequest(request);

        List<Order> orders = orderRepository.findByUserId(request.getUserId());

        List<GetOrderResponse> orderResponses = orders.stream()
                .map(order -> new GetOrderResponse(
                        order.getId(),
                        order.getUserId(),
                        order.getItems(),
                        order.getTotal(),
                        order.getStatus(),
                        order.getCreatedAt(),
                        order.getUpdatedAt()
                ))
                .collect(java.util.stream.Collectors.toList());

        return new GetOrderListResponse(orderResponses, orderResponses.size());
    }

    private void validateRequest(GetOrderRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getOrderId() == null) {
            throw new BusinessException("Order ID is required");
        }
    }

    private void validateUserOrdersRequest(GetUserOrdersRequest request) {
        if (request == null) {
            throw new BusinessException("Request cannot be null");
        }
        if (request.getUserId() == null) {
            throw new BusinessException("User ID is required");
        }
    }
}
