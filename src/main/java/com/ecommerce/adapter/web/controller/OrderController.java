package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateOrderRequestDto;
import com.ecommerce.adapter.web.dto.response.OrderResponseDto;
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

    // TODO: Inject use cases when implemented
    // private final CreateOrderUseCase createOrderUseCase;
    // private final GetOrderUseCase getOrderUseCase;
    // private final GetUserOrdersUseCase getUserOrdersUseCase;
    // private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    // private final CancelOrderUseCase cancelOrderUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @Valid @RequestBody CreateOrderRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(null, "Order created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponseDto>>> getUserOrders(
            Authentication authentication,
            Pageable pageable) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Orders retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(
            @PathVariable @NotNull @Positive Long id,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Order retrieved successfully"));
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
}