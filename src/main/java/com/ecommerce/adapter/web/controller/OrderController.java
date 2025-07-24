package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateOrderRequestDto;
import com.ecommerce.adapter.web.dto.response.OrderResponseDto;
import com.ecommerce.core.usecase.order.CreateOrderUseCase;
import com.ecommerce.core.usecase.order.CreateOrderRequest;
import com.ecommerce.core.usecase.order.CreateOrderResponse;
import com.ecommerce.core.usecase.order.GetOrderUseCase;
import com.ecommerce.core.usecase.order.GetOrderRequest;
import com.ecommerce.core.usecase.order.GetOrderResponse;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/orders")
@Validated
@PreAuthorize("isAuthenticated()")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final UserRepository userRepository;

    public OrderController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase, 
                          UserRepository userRepository) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @Valid @RequestBody CreateOrderRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        CreateOrderRequest request = new CreateOrderRequest(
            userId,
            new CreateOrderRequest.AddressRequest(
                requestDto.getShippingAddress().getStreet(),
                requestDto.getShippingAddress().getCity(),
                requestDto.getShippingAddress().getState(),
                requestDto.getShippingAddress().getZipCode(),
                requestDto.getShippingAddress().getCountry()
            ),
            new CreateOrderRequest.AddressRequest(
                requestDto.getBillingAddress().getStreet(),
                requestDto.getBillingAddress().getCity(),
                requestDto.getBillingAddress().getState(),
                requestDto.getBillingAddress().getZipCode(),
                requestDto.getBillingAddress().getCountry()
            )
        );
        
        CreateOrderResponse response = createOrderUseCase.execute(request);
        
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(response.getOrderId());
        responseDto.setUserId(userId);
        responseDto.setStatus(response.getStatus());
        responseDto.setTotalAmount(response.getTotalAmount());
        responseDto.setCurrency(response.getCurrency());
        responseDto.setCreatedAt(java.time.LocalDateTime.now());
        responseDto.setUpdatedAt(java.time.LocalDateTime.now());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(responseDto, response.getMessage()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getUserOrders(
            Authentication authentication,
            Pageable pageable) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        Page<GetOrderResponse> orders = getOrderUseCase.getUserOrders(userId, pageable);
        Page<OrderResponseDto> orderDtos = orders.map(this::mapToOrderResponseDto);
        
        return ResponseEntity.ok(ApiResponse.success(orderDtos, "Orders retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);
        
        GetOrderRequest request = new GetOrderRequest(id, userId);
        GetOrderResponse response = getOrderUseCase.execute(request);
        OrderResponseDto orderDto = mapToOrderResponseDto(response);
        
        return ResponseEntity.ok(ApiResponse.success(orderDto, "Order retrieved successfully"));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Order cancelled successfully"));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> getOrderStatus(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success("PENDING", "Order status retrieved successfully"));
    }

    // Admin endpoints
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getAllOrders(
            Pageable pageable,
            @RequestParam(required = false) String status) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "All orders retrieved successfully"));
    }

    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrderStatus(
            @PathVariable @NotNull @Positive Long id,
            @RequestParam String status) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Order status updated successfully"));
    }

    private Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(new com.ecommerce.core.domain.user.valueobject.Email(email))
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getId();
    }

    private OrderResponseDto mapToOrderResponseDto(GetOrderResponse response) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(response.getId());
        dto.setUserId(response.getUserId());
        dto.setStatus(response.getStatus());
        dto.setTotalAmount(response.getTotalAmount());
        dto.setSubtotalAmount(response.getSubtotalAmount());
        dto.setTaxAmount(response.getTaxAmount());
        dto.setShippingAmount(response.getShippingAmount());
        dto.setDiscountAmount(response.getDiscountAmount());
        dto.setCurrency(response.getCurrency());
        dto.setTrackingNumber(response.getTrackingNumber());
        dto.setEstimatedDelivery(response.getEstimatedDelivery());
        dto.setCreatedAt(response.getCreatedAt());
        dto.setUpdatedAt(response.getUpdatedAt());

        // Map shipping address
        if (response.getShippingAddress() != null) {
            OrderResponseDto.AddressDto shippingDto = new OrderResponseDto.AddressDto();
            shippingDto.setStreet(response.getShippingAddress().getStreet());
            shippingDto.setCity(response.getShippingAddress().getCity());
            shippingDto.setState(response.getShippingAddress().getState());
            shippingDto.setZipCode(response.getShippingAddress().getZipCode());
            shippingDto.setCountry(response.getShippingAddress().getCountry());
            dto.setShippingAddress(shippingDto);
        }

        // Map billing address
        if (response.getBillingAddress() != null) {
            OrderResponseDto.AddressDto billingDto = new OrderResponseDto.AddressDto();
            billingDto.setStreet(response.getBillingAddress().getStreet());
            billingDto.setCity(response.getBillingAddress().getCity());
            billingDto.setState(response.getBillingAddress().getState());
            billingDto.setZipCode(response.getBillingAddress().getZipCode());
            billingDto.setCountry(response.getBillingAddress().getCountry());
            dto.setBillingAddress(billingDto);
        }

        // Map order items
        if (response.getItems() != null) {
            java.util.List<OrderResponseDto.OrderItemDto> itemDtos = response.getItems().stream()
                .map(item -> {
                    OrderResponseDto.OrderItemDto itemDto = new OrderResponseDto.OrderItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setProductId(item.getProductId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setProductSku(item.getProductSku());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setUnitPrice(item.getUnitPrice());
                    itemDto.setTotalPrice(item.getTotalPrice());
                    return itemDto;
                })
                .collect(java.util.stream.Collectors.toList());
            dto.setItems(itemDtos);
        }

        return dto;
    }
}