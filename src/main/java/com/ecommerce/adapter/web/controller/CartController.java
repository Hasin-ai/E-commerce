package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.AddCartItemRequestDto;
import com.ecommerce.adapter.web.dto.request.UpdateCartItemRequestDto;
import com.ecommerce.adapter.web.dto.response.CartResponseDto;
import com.ecommerce.core.usecase.cart.GetCartUseCase;
import com.ecommerce.core.usecase.cart.GetCartRequest;
import com.ecommerce.core.usecase.cart.GetCartResponse;
import com.ecommerce.core.usecase.cart.AddCartItemUseCase;
import com.ecommerce.core.usecase.cart.AddCartItemRequest;
import com.ecommerce.core.usecase.cart.AddCartItemResponse;
import com.ecommerce.core.usecase.cart.UpdateCartItemUseCase;
import com.ecommerce.core.usecase.cart.UpdateCartItemRequest;
import com.ecommerce.core.usecase.cart.UpdateCartItemResponse;
import com.ecommerce.core.usecase.cart.RemoveCartItemUseCase;
import com.ecommerce.core.usecase.cart.RemoveCartItemRequest;
import com.ecommerce.core.usecase.cart.RemoveCartItemResponse;
import com.ecommerce.core.usecase.cart.ClearCartUseCase;
import com.ecommerce.core.usecase.cart.ClearCartRequest;
import com.ecommerce.core.usecase.cart.ClearCartResponse;
import com.ecommerce.core.domain.user.repository.UserRepository;
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

    private final GetCartUseCase getCartUseCase;
    private final AddCartItemUseCase addCartItemUseCase;
    private final UpdateCartItemUseCase updateCartItemUseCase;
    private final RemoveCartItemUseCase removeCartItemUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final UserRepository userRepository;

    public CartController(GetCartUseCase getCartUseCase, AddCartItemUseCase addCartItemUseCase,
                         UpdateCartItemUseCase updateCartItemUseCase, RemoveCartItemUseCase removeCartItemUseCase,
                         ClearCartUseCase clearCartUseCase, UserRepository userRepository) {
        this.getCartUseCase = getCartUseCase;
        this.addCartItemUseCase = addCartItemUseCase;
        this.updateCartItemUseCase = updateCartItemUseCase;
        this.removeCartItemUseCase = removeCartItemUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(Authentication authentication) {
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);

        GetCartRequest request = new GetCartRequest(userId);
        GetCartResponse response = getCartUseCase.execute(request);

        CartResponseDto cartDto = mapToCartResponseDto(response);

        return ResponseEntity.ok(ApiResponse.success(cartDto, "Cart retrieved successfully"));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartResponseDto>> addItemToCart(
            @Valid @RequestBody AddCartItemRequestDto requestDto,
            Authentication authentication) {

        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);

        AddCartItemRequest request = new AddCartItemRequest(
            userId,
            requestDto.getProductId(),
            requestDto.getQuantity()
        );

        AddCartItemResponse response = addCartItemUseCase.execute(request);
        CartResponseDto cartDto = mapToCartResponseDto(response);

        return ResponseEntity.status(201).body(ApiResponse.success(cartDto, response.getMessage()));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateCartItem(
            @PathVariable @NotNull @Positive Long itemId,
            @Valid @RequestBody UpdateCartItemRequestDto requestDto,
            Authentication authentication) {

        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);

        UpdateCartItemRequest request = UpdateCartItemRequest.of(userId, itemId, requestDto.getQuantity());
        UpdateCartItemResponse response = updateCartItemUseCase.execute(request);

        if (response.isSuccess()) {
            CartResponseDto cartDto = mapToCartResponseDto(response.getCart());
            return ResponseEntity.ok(ApiResponse.success(cartDto, response.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage()));
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeCartItem(
            @PathVariable @NotNull @Positive Long itemId,
            Authentication authentication) {

        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);

        RemoveCartItemRequest request = RemoveCartItemRequest.byCartItemId(userId, itemId);
        RemoveCartItemResponse response = removeCartItemUseCase.execute(request);

        if (response.isSuccess()) {
            CartResponseDto cartDto = mapToCartResponseDto(response.getCart());
            return ResponseEntity.ok(ApiResponse.success(cartDto, response.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage()));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(Authentication authentication) {
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);

        ClearCartRequest request = ClearCartRequest.forUser(userId);
        ClearCartResponse response = clearCartUseCase.execute(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(null, response.getMessage()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getCartItemCount(Authentication authentication) {
        String userEmail = authentication.getName();
        Long userId = getUserIdFromEmail(userEmail);

        GetCartRequest request = new GetCartRequest(userId);
        GetCartResponse response = getCartUseCase.execute(request);

        return ResponseEntity.ok(ApiResponse.success(response.getTotalItems(), "Cart item count retrieved successfully"));
    }

    private Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(new com.ecommerce.core.domain.user.valueobject.Email(email))
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getId();
    }

    private CartResponseDto mapToCartResponseDto(GetCartResponse response) {
        CartResponseDto dto = new CartResponseDto();
        dto.setId(response.getId());
        dto.setUserId(response.getUserId());
        dto.setTotalAmount(response.getTotalAmount());
        dto.setTotalItems(response.getTotalItems());
        dto.setCreatedAt(response.getCreatedAt());
        dto.setUpdatedAt(response.getUpdatedAt());

        // Map cart items
        if (response.getItems() != null) {
            java.util.List<CartResponseDto.CartItemDto> itemDtos = response.getItems().stream()
                .map(item -> {
                    CartResponseDto.CartItemDto itemDto = new CartResponseDto.CartItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setProductId(item.getProductId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setProductSku(null); // CartItem doesn't have SKU
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

    private CartResponseDto mapToCartResponseDto(AddCartItemResponse response) {
        CartResponseDto dto = new CartResponseDto();
        dto.setId(response.getCartId());
        dto.setUserId(response.getUserId());
        dto.setTotalAmount(response.getTotalAmount());
        dto.setTotalItems(response.getTotalItems());

        // Map cart items
        if (response.getItems() != null) {
            java.util.List<CartResponseDto.CartItemDto> itemDtos = response.getItems().stream()
                .map(item -> {
                    CartResponseDto.CartItemDto itemDto = new CartResponseDto.CartItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setProductId(item.getProductId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setProductSku(null); // CartItem doesn't have SKU
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

    private CartResponseDto mapToCartResponseDto(com.ecommerce.core.domain.cart.entity.Cart cart) {
        CartResponseDto dto = new CartResponseDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setTotalAmount(cart.getTotalAmount());
        dto.setTotalItems(cart.getTotalItems());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        
        // Map cart items
        if (cart.getItems() != null) {
            java.util.List<CartResponseDto.CartItemDto> itemDtos = cart.getItems().stream()
                .map(item -> {
                    CartResponseDto.CartItemDto itemDto = new CartResponseDto.CartItemDto();
                    itemDto.setId(item.getId());
                    itemDto.setProductId(item.getProductId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setProductSku(null); // CartItem doesn't have SKU
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
