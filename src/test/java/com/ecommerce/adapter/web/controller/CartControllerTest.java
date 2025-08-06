package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.AddCartItemRequestDto;
import com.ecommerce.config.TestSecurityConfig;
import com.ecommerce.core.usecase.cart.*;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@Import(TestSecurityConfig.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GetCartUseCase getCartUseCase;

    @MockBean
    private AddCartItemUseCase addCartItemUseCase;

    @MockBean
    private RemoveCartItemUseCase removeCartItemUseCase;

    @MockBean
    private ClearCartUseCase clearCartUseCase;

    @MockBean
    private UserRepository userRepository;

    private User mockUser;
    private Cart mockCart;
    private CartItem mockCartItem;

    @BeforeEach
    void setUp() {
        mockUser = new User("test@example.com", "password123");
        mockUser.setId(1L);

        mockCartItem = new CartItem(1L, "Gaming Laptop", BigDecimal.valueOf(1299.99), 1);
        
        mockCart = new Cart(1L);
        mockCart.setId(1L);
        mockCart.addItem(mockCartItem);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getCart_WithValidUser_ShouldReturnCart() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        GetCartResponse response = GetCartResponse.builder()
                .id(1L)
                .userId(1L)
                .items(Arrays.asList(mockCartItem))
                .totalAmount(BigDecimal.valueOf(1299.99))
                .totalItems(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        when(getCartUseCase.execute(any(GetCartRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/cart")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.totalAmount").value(1299.99))
                .andExpect(jsonPath("$.data.totalItems").value(1));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void addItemToCart_WithValidRequest_ShouldAddItem() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto();
        requestDto.setProductId(1L);
        requestDto.setQuantity(2);

        AddCartItemResponse response = AddCartItemResponse.builder()
                .cartId(1L)
                .userId(1L)
                .items(Arrays.asList(mockCartItem))
                .totalAmount(BigDecimal.valueOf(2599.98))
                .totalItems(2)
                .message("Item added to cart successfully")
                .build();

        when(addCartItemUseCase.execute(any(AddCartItemRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalItems").value(2))
                .andExpect(jsonPath("$.message").value("Item added to cart successfully"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void addItemToCart_WithInvalidQuantity_ShouldReturnBadRequest() throws Exception {
        // Given
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto();
        requestDto.setProductId(1L);
        requestDto.setQuantity(0); // Invalid quantity

        // When & Then
        mockMvc.perform(post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void removeCartItem_WithValidItemId_ShouldRemoveItem() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        RemoveCartItemResponse response = RemoveCartItemResponse.success(mockCart);
        when(removeCartItemUseCase.execute(any(RemoveCartItemRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(delete("/api/cart/items/1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Item removed from cart successfully"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void removeCartItem_WithInvalidItemId_ShouldReturnBadRequest() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        RemoveCartItemResponse response = RemoveCartItemResponse.failure("Cart item not found");
        when(removeCartItemUseCase.execute(any(RemoveCartItemRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(delete("/api/cart/items/999")
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cart item not found"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void clearCart_WithValidUser_ShouldClearCart() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        ClearCartResponse response = ClearCartResponse.success();
        when(clearCartUseCase.execute(any(ClearCartRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(delete("/api/cart")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cart cleared successfully"));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void getCartItemCount_WithValidUser_ShouldReturnCount() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        GetCartResponse response = GetCartResponse.builder()
                .totalItems(3)
                .build();
        
        when(getCartUseCase.execute(any(GetCartRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/cart/count")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(3))
                .andExpect(jsonPath("$.message").value("Cart item count retrieved successfully"));
    }

    @Test
    void getCart_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addItemToCart_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        AddCartItemRequestDto requestDto = new AddCartItemRequestDto();
        requestDto.setProductId(1L);
        requestDto.setQuantity(1);

        mockMvc.perform(post("/api/cart/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "nonexistent@example.com")
    void getCart_WithNonexistentUser_ShouldReturnError() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/cart")
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }
}