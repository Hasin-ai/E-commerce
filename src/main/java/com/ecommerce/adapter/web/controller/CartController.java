package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.AddCartItemRequestDto;
import com.ecommerce.adapter.web.dto.request.UpdateCartItemRequestDto;
import com.ecommerce.adapter.web.dto.response.CartResponseDto;
import com.ecommerce.adapter.web.mapper.CartMapper;
import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.usecase.cart.AddItemToCartUseCase;
import com.ecommerce.core.usecase.cart.GetCartUseCase;
import com.ecommerce.core.usecase.cart.RemoveItemFromCartUseCase;
import com.ecommerce.core.usecase.cart.UpdateCartItemUseCase;
import com.ecommerce.infrastructure.security.CustomUserDetailsService;
import com.ecommerce.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final AddItemToCartUseCase addItemToCartUseCase;
    private final GetCartUseCase getCartUseCase;
    private final RemoveItemFromCartUseCase removeItemFromCartUseCase;
    private final UpdateCartItemUseCase updateCartItemUseCase;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> addItemToCart(@AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal userPrincipal,
                                                                      @Valid @RequestBody AddCartItemRequestDto request) {
        Long userId = userPrincipal.getUser().getId();
        Cart cart = addItemToCartUseCase.execute(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success(cartMapper.toDto(cart)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(@AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUser().getId();
        Cart cart = getCartUseCase.execute(userId);
        return ResponseEntity.ok(ApiResponse.success(cartMapper.toDto(cart)));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeItemFromCart(@AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal userPrincipal,
                                                                              @PathVariable Long productId) {
        Long userId = userPrincipal.getUser().getId();
        Cart cart = removeItemFromCartUseCase.execute(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(cartMapper.toDto(cart)));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateCartItem(@AuthenticationPrincipal CustomUserDetailsService.CustomUserPrincipal userPrincipal,
                                                                        @PathVariable Long productId,
                                                                        @Valid @RequestBody UpdateCartItemRequestDto request) {
        Long userId = userPrincipal.getUser().getId();
        Cart cart = updateCartItemUseCase.execute(userId, productId, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success(cartMapper.toDto(cart)));
    }
}