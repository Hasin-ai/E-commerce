package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import com.ecommerce.core.domain.search.entity.SearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class SearchProductsUseCaseTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private SearchProductsUseCase searchProductsUseCase;

    private SearchProductsRequest request;
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

        request = new SearchProductsRequest();
        request.setQuery("laptop");
        request.setPage(0);
        request.setSize(10);
    }

    @Test
    @DisplayName("Should search products successfully")
    void shouldSearchProductsSuccessfully() {
        // Given
        SearchResult searchResult = new SearchResult();
        searchResult.setProducts(Arrays.asList(product1, product2));
        searchResult.setTotalResults(2L);
        
        when(searchRepository.searchProducts(any())).thenReturn(searchResult);

        // When
        SearchProductsResponse response = searchProductsUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(2, response.getProducts().size());
        assertEquals(2L, response.getTotalResults());
        verify(searchRepository).searchProducts(any());
    }

    @Test
    @DisplayName("Should handle empty search results")
    void shouldHandleEmptySearchResults() {
        // Given
        SearchResult searchResult = new SearchResult();
        searchResult.setProducts(Collections.emptyList());
        searchResult.setTotalResults(0L);
        
        when(searchRepository.searchProducts(any())).thenReturn(searchResult);

        // When
        SearchProductsResponse response = searchProductsUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getProducts().isEmpty());
        assertEquals(0L, response.getTotalResults());
    }

    @Test
    @DisplayName("Should search with filters")
    void shouldSearchWithFilters() {
        // Given
        request.setCategory("Electronics");
        request.setMinPrice(BigDecimal.valueOf(500.00));
        request.setMaxPrice(BigDecimal.valueOf(1500.00));
        
        SearchResult searchResult = new SearchResult();
        searchResult.setProducts(Arrays.asList(product1));
        searchResult.setTotalResults(1L);
        
        when(searchRepository.searchProducts(any())).thenReturn(searchResult);

        // When
        SearchProductsResponse response = searchProductsUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1, response.getProducts().size());
        verify(searchRepository).searchProducts(argThat(criteria -> 
            criteria.getCategory().equals("Electronics") &&
            criteria.getMinPrice().equals(BigDecimal.valueOf(500.00)) &&
            criteria.getMaxPrice().equals(BigDecimal.valueOf(1500.00))
        ));
    }

    @Test
    @DisplayName("Should handle search repository exception")
    void shouldHandleSearchRepositoryException() {
        // Given
        when(searchRepository.searchProducts(any())).thenThrow(new RuntimeException("Search service unavailable"));

        // When
        SearchProductsResponse response = searchProductsUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getErrorMessage().contains("Search service unavailable"));
    }

    @Test
    @DisplayName("Should validate search query")
    void shouldValidateSearchQuery() {
        // Given
        request.setQuery("");

        // When
        SearchProductsResponse response = searchProductsUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Search query cannot be empty", response.getErrorMessage());
        verify(searchRepository, never()).searchProducts(any());
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void shouldHandlePaginationCorrectly() {
        // Given
        request.setPage(1);
        request.setSize(5);
        
        SearchResult searchResult = new SearchResult();
        searchResult.setProducts(Arrays.asList(product2));
        searchResult.setTotalResults(6L);
        
        when(searchRepository.searchProducts(any())).thenReturn(searchResult);

        // When
        SearchProductsResponse response = searchProductsUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1, response.getProducts().size());
        assertEquals(6L, response.getTotalResults());
        verify(searchRepository).searchProducts(argThat(criteria -> 
            criteria.getPage() == 1 && criteria.getSize() == 5
        ));
    }
}
