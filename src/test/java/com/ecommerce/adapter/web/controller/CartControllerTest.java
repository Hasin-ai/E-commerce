package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.cart.AddCartItemUseCase;
import com.ecommerce.core.usecase.cart.GetCartUseCase;
import com.ecommerce.core.usecase.cart.UpdateCartItemUseCase;
import com.ecommerce.core.usecase.cart.RemoveCartItemUseCase;
import com.ecommerce.core.usecase.cart.AddCartItemRequest;
import com.ecommerce.core.usecase.cart.AddCartItemResponse;
import com.ecommerce.core.usecase.cart.GetCartRequest;
import com.ecommerce.core.usecase.cart.GetCartResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddCartItemUseCase addCartItemUseCase;

    @MockBean
    private GetCartUseCase getCartUseCase;

    @MockBean
    private UpdateCartItemUseCase updateCartItemUseCase;

    @MockBean
    private RemoveCartItemUseCase removeCartItemUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private AddCartItemRequest addItemRequest;

    @BeforeEach
    void setUp() {
        addItemRequest = new AddCartItemRequest();
        addItemRequest.setUserId(1L);
        addItemRequest.setProductId(1L);
        addItemRequest.setQuantity(2);
    }

    @Test
    @DisplayName("Should add item to cart successfully")
    void shouldAddItemToCartSuccessfully() throws Exception {
        // Given
        AddCartItemResponse response = new AddCartItemResponse();
        response.setSuccess(true);
        response.setMessage("Item added to cart");

        when(addCartItemUseCase.execute(any(AddCartItemRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addItemRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Item added to cart"));
    }

    @Test
    @DisplayName("Should get cart for user")
    void shouldGetCartForUser() throws Exception {
        // Given
        GetCartResponse response = new GetCartResponse();
        response.setSuccess(true);
        response.setUserId(1L);
        response.setTotalItems(3);

        when(getCartUseCase.execute(any(GetCartRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/cart/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.totalItems").value(3));
    }

    @Test
    @DisplayName("Should handle add item failure")
    void shouldHandleAddItemFailure() throws Exception {
        // Given
        AddCartItemResponse response = new AddCartItemResponse();
        response.setSuccess(false);
        response.setErrorMessage("Product not available");

        when(addCartItemUseCase.execute(any(AddCartItemRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addItemRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Product not available"));
    }

    @Test
    @DisplayName("Should remove item from cart")
    void shouldRemoveItemFromCart() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/cart/1/items/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should validate request parameters")
    void shouldValidateRequestParameters() throws Exception {
        // Given
        AddCartItemRequest invalidRequest = new AddCartItemRequest();
        invalidRequest.setQuantity(-1); // Invalid quantity

        // When & Then
        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
