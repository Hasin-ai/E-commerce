package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateOrderRequestDto;
import com.ecommerce.adapter.web.dto.response.OrderResponseDto;
import com.ecommerce.adapter.web.mapper.OrderMapper;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.usecase.order.CreateOrderUseCase;
import com.ecommerce.core.usecase.order.CreateOrderRequest;
import com.ecommerce.core.usecase.order.CreateOrderResponse;
import com.ecommerce.core.usecase.order.PlaceOrderUseCase;
import com.ecommerce.core.usecase.order.PlaceOrderRequest;
import com.ecommerce.core.usecase.order.PlaceOrderResponse;
import com.ecommerce.shared.dto.ApiResponse;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@Valid @RequestBody CreateOrderRequestDto requestDto) {
        // First create the order
        CreateOrderRequest createRequest = orderMapper.toCreateOrderRequest(requestDto);
        CreateOrderResponse createResponse = createOrderUseCase.execute(createRequest);

        // Then place the order
        PlaceOrderRequest placeRequest = orderMapper.toPlaceOrderRequest(createResponse.getUserId(), createResponse.getId());
        PlaceOrderResponse placeResponse = placeOrderUseCase.execute(placeRequest);

        Order order = orderRepository.findById(placeResponse.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found after creation"));
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto, "Order created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        OrderResponseDto responseDto = orderMapper.toOrderResponseDto(order);
        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }
}
