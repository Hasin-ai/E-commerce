package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.search.SearchProductsUseCase;
import com.ecommerce.core.usecase.search.GetRecommendationsUseCase;
import com.ecommerce.core.usecase.search.SearchProductsRequest;
import com.ecommerce.core.usecase.search.SearchProductsResponse;
import com.ecommerce.core.usecase.search.GetRecommendationsRequest;
import com.ecommerce.core.usecase.search.GetRecommendationsResponse;
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

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchProductsUseCase searchProductsUseCase;

    @MockBean
    private GetRecommendationsUseCase getRecommendationsUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Gaming Laptop");
        product1.setPrice(BigDecimal.valueOf(1200.00));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Office Laptop");
        product2.setPrice(BigDecimal.valueOf(800.00));
    }

    @Test
    @DisplayName("Should search products successfully")
    void shouldSearchProductsSuccessfully() throws Exception {
        // Given
        SearchProductsResponse response = new SearchProductsResponse();
        response.setSuccess(true);
        response.setProducts(Arrays.asList(product1, product2));
        response.setTotalResults(2L);

        when(searchProductsUseCase.execute(any(SearchProductsRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/products")
                .param("query", "laptop")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.totalResults").value(2));
    }

    @Test
    @DisplayName("Should search products with filters")
    void shouldSearchProductsWithFilters() throws Exception {
        // Given
        SearchProductsResponse response = new SearchProductsResponse();
        response.setSuccess(true);
        response.setProducts(Arrays.asList(product1));
        response.setTotalResults(1L);

        when(searchProductsUseCase.execute(any(SearchProductsRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/products")
                .param("query", "gaming")
                .param("category", "Electronics")
                .param("minPrice", "1000")
                .param("maxPrice", "1500")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.products[0].name").value("Gaming Laptop"));
    }

    @Test
    @DisplayName("Should get product recommendations")
    void shouldGetProductRecommendations() throws Exception {
        // Given
        GetRecommendationsResponse response = new GetRecommendationsResponse();
        response.setSuccess(true);
        response.setRecommendedProducts(Arrays.asList(product1, product2));

        when(getRecommendationsUseCase.execute(any(GetRecommendationsRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/recommendations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.recommendedProducts").isArray())
                .andExpect(jsonPath("$.recommendedProducts[0].name").value("Gaming Laptop"));
    }

    @Test
    @DisplayName("Should handle empty search results")
    void shouldHandleEmptySearchResults() throws Exception {
        // Given
        SearchProductsResponse response = new SearchProductsResponse();
        response.setSuccess(true);
        response.setProducts(Arrays.asList());
        response.setTotalResults(0L);

        when(searchProductsUseCase.execute(any(SearchProductsRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/products")
                .param("query", "nonexistent")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.products").isEmpty())
                .andExpect(jsonPath("$.totalResults").value(0));
    }

    @Test
    @DisplayName("Should handle search service failure")
    void shouldHandleSearchServiceFailure() throws Exception {
        // Given
        SearchProductsResponse response = new SearchProductsResponse();
        response.setSuccess(false);
        response.setErrorMessage("Search service unavailable");

        when(searchProductsUseCase.execute(any(SearchProductsRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/products")
                .param("query", "laptop")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Search service unavailable"));
    }
}
