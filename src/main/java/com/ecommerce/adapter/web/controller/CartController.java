package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.AddCartItemRequestDto;
import com.ecommerce.adapter.web.dto.request.UpdateCartItemRequestDto;
import com.ecommerce.adapter.web.dto.response.CartResponseDto;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/cart")
@Validated
@PreAuthorize("isAuthenticated()")
public class CartController {

    // TODO: Inject use cases when implemented
    // private final GetCartUseCase getCartUseCase;
    // private final AddCartItemUseCase addCartItemUseCase;
    // private final UpdateCartItemUseCase updateCartItemUseCase;
    // private final RemoveCartItemUseCase removeCartItemUseCase;
    // private final ClearCartUseCase clearCartUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(Authentication authentication) {
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Cart retrieved successfully"));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponseDto>> addItemToCart(
            @Valid @RequestBody AddCartItemRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Item added to cart successfully"));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateCartItem(
            @PathVariable @NotNull @Positive Long itemId,
            @Valid @RequestBody UpdateCartItemRequestDto requestDto,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Cart item updated successfully"));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeCartItem(
            @PathVariable @NotNull @Positive Long itemId,
            Authentication authentication) {
        
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Item removed from cart successfully"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(Authentication authentication) {
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared successfully"));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getCartItemCount(Authentication authentication) {
        String userEmail = authentication.getName();
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(0, "Cart item count retrieved successfully"));
    }
}