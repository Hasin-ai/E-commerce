package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.product.GetProductUseCase;
import com.ecommerce.core.usecase.product.GetProductsUseCase;
import com.ecommerce.core.usecase.product.GetProductRequest;
import com.ecommerce.core.usecase.product.GetProductResponse;
import com.ecommerce.core.usecase.product.GetProductsRequest;
import com.ecommerce.core.domain.product.entity.Product;
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
import java.math.BigDecimal;
import java.util.Arrays;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetProductUseCase getProductUseCase;

    @MockBean
    private GetProductsUseCase getProductsUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("A test product");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setAvailable(true);
    }

    @Test
    @DisplayName("Should get product by ID successfully")
    void shouldGetProductByIdSuccessfully() throws Exception {
        // Given
        GetProductResponse response = new GetProductResponse();
        response.setSuccess(true);
        response.setProduct(product);

        when(getProductUseCase.execute(any(GetProductRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.product.id").value(1L))
                .andExpect(jsonPath("$.product.name").value("Test Product"));
    }

    @Test
    @DisplayName("Should handle product not found")
    void shouldHandleProductNotFound() throws Exception {
        // Given
        GetProductResponse response = new GetProductResponse();
        response.setSuccess(false);
        response.setErrorMessage("Product not found");

        when(getProductUseCase.execute(any(GetProductRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/products/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Product not found"));
    }

    @Test
    @DisplayName("Should get all products with pagination")
    void shouldGetAllProductsWithPagination() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should filter products by category")
    void shouldFilterProductsByCategory() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                .param("category", "Electronics")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should filter products by price range")
    void shouldFilterProductsByPriceRange() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                .param("minPrice", "10.00")
                .param("maxPrice", "100.00")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should search products by name")
    void shouldSearchProductsByName() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products")
                .param("search", "laptop")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
