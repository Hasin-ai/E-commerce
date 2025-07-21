package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.request.CreateOrderRequestDto;
import com.ecommerce.adapter.web.dto.response.OrderItemResponseDto;
import com.ecommerce.adapter.web.dto.response.OrderResponseDto;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.entity.OrderItem;
import com.ecommerce.core.usecase.order.PlaceOrderRequest;
import com.ecommerce.core.usecase.order.CreateOrderRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public PlaceOrderRequest toPlaceOrderRequest(Long userId, Long orderId) {
        return PlaceOrderRequest.builder()
                .userId(userId)
                .orderId(orderId)
                .build();
    }

    public CreateOrderRequest toCreateOrderRequest(CreateOrderRequestDto dto) {
        return CreateOrderRequest.builder()
                .userId(dto.getUserId())
                .items(dto.getItems().stream()
                    .map(item -> CreateOrderRequest.OrderItemRequest.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .build())
                    .collect(Collectors.toList()))
                .build();
    }

    public OrderResponseDto toOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .items(order.getItems().stream().map(this::toOrderItemResponseDto).collect(Collectors.toList()))
                .total(order.getTotal().getAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice().getAmount())
                .totalPrice(orderItem.getTotalPrice().getAmount())
                .build();
    }
}
