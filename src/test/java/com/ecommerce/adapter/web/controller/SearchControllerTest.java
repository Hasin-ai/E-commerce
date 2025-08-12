package com.ecommerce.adapter.web.controller;

import com.ecommerce.config.TestSecurityConfig;
import com.ecommerce.core.usecase.search.SearchProductsUseCase;
import com.ecommerce.core.usecase.search.GetRecommendationsUseCase;
import com.ecommerce.core.usecase.analytics.TrackEventUseCase;
import com.ecommerce.core.usecase.search.SearchProductsResponse;
import com.ecommerce.core.usecase.search.GetRecommendationsResponse;
import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.domain.recommendation.entity.ProductRecommendation;
import com.ecommerce.core.domain.recommendation.entity.RecommendationType;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@Import(TestSecurityConfig.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SearchProductsUseCase searchProductsUseCase;

    @MockBean
    private GetRecommendationsUseCase getRecommendationsUseCase;

    @MockBean
    private TrackEventUseCase trackEventUseCase;

    private SearchResult mockSearchResult;
    private ProductRecommendation mockRecommendation;

    @BeforeEach
    void setUp() {
        mockSearchResult = SearchResult.builder()
                .id(1L)
                .name("Gaming Laptop")
                .description("High-performance gaming laptop")
                .sku("LAPTOP-001")
                .price(BigDecimal.valueOf(1299.99))
                .imageUrl("/images/laptop.jpg")
                .category("Electronics")
                .score(0.95)
                .inStock(true)
                .build();

        mockRecommendation = ProductRecommendation.builder()
                .id(1L)
                .productId(1L)
                .productName("Gaming Mouse")
                .productImageUrl("/images/mouse.jpg")
                .productPrice(BigDecimal.valueOf(79.99))
                .type(RecommendationType.PERSONALIZED)
                .score(0.85)
                .reason("Based on your browsing history")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void searchProducts_WithValidQuery_ShouldReturnResults() throws Exception {
        // Given
        List<SearchResult> searchResults = Arrays.asList(mockSearchResult);
        SearchProductsResponse response = SearchProductsResponse.builder()
                .products(searchResults)
                .totalElements(1)
                .totalPages(1)
                .build();

        when(searchProductsUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/products")
                .param("query", "laptop")
                .param("page", "0")
                .param("size", "20")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products").isArray())
                .andExpect(jsonPath("$.data.products[0].name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.query").value("laptop"));
    }

    @Test
    void searchProducts_WithFilters_ShouldReturnFilteredResults() throws Exception {
        // Given
        List<SearchResult> searchResults = Arrays.asList(mockSearchResult);
        SearchProductsResponse response = SearchProductsResponse.builder()
                .products(searchResults)
                .totalElements(1)
                .totalPages(1)
                .build();

        when(searchProductsUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/products")
                .param("query", "laptop")
                .param("category", "Electronics")
                .param("minPrice", "1000")
                .param("maxPrice", "2000")
                .param("sortBy", "price")
                .param("sortDirection", "asc")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.products[0].category").value("Electronics"));
    }

    @Test
    void searchProducts_WithEmptyQuery_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/search/products")
                .param("query", "")
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getRecommendations_WithAuthenticatedUser_ShouldReturnPersonalizedRecommendations() throws Exception {
        // Given
        List<ProductRecommendation> recommendations = Arrays.asList(mockRecommendation);
        GetRecommendationsResponse response = GetRecommendationsResponse.success(recommendations, "PERSONALIZED");

        when(getRecommendationsUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/recommendations")
                .param("limit", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].productName").value("Gaming Mouse"))
                .andExpect(jsonPath("$.data[0].recommendationType").value("PERSONALIZED"));
    }

    @Test
    void getRecommendations_WithAnonymousUser_ShouldReturnTrendingRecommendations() throws Exception {
        // Given
        ProductRecommendation trendingRecommendation = ProductRecommendation.builder()
                .id(2L)
                .productId(2L)
                .productName("Trending Product")
                .productPrice(BigDecimal.valueOf(99.99))
                .type(RecommendationType.TRENDING)
                .score(0.9)
                .reason("Trending now")
                .build();

        List<ProductRecommendation> recommendations = Arrays.asList(trendingRecommendation);
        GetRecommendationsResponse response = GetRecommendationsResponse.success(recommendations, "TRENDING");

        when(getRecommendationsUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/recommendations")
                .param("limit", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].recommendationType").value("TRENDING"));
    }

    @Test
    void getRecommendations_WithInvalidLimit_ShouldUseDefaultLimit() throws Exception {
        // Given
        List<ProductRecommendation> recommendations = Arrays.asList(mockRecommendation);
        GetRecommendationsResponse response = GetRecommendationsResponse.success(recommendations, "TRENDING");

        when(getRecommendationsUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/recommendations")
                .param("limit", "-1")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void searchProducts_WithLargePageSize_ShouldLimitResults() throws Exception {
        // Given
        List<SearchResult> searchResults = Arrays.asList(mockSearchResult);
        SearchProductsResponse response = SearchProductsResponse.builder()
                .products(searchResults)
                .totalElements(1)
                .totalPages(1)
                .build();

        when(searchProductsUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/search/products")
                .param("query", "laptop")
                .param("size", "1000")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pageSize").value(1000)); // Should be limited by backend
    }
}