package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.search.entity.SearchCriteria;
import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {
    
    @Override
    public List<SearchResult> searchProducts(com.ecommerce.core.usecase.search.SearchProductsRequest request) {
        // Mock implementation - replace with actual database search
        return createMockSearchResults(request);
    }
    
    @Override
    public int countSearchResults(com.ecommerce.core.usecase.search.SearchProductsRequest request) {
        // Mock implementation - return count of search results
        return searchProducts(request).size();
    }
    
    private List<SearchResult> createMockSearchResults(com.ecommerce.core.usecase.search.SearchProductsRequest request) {
        List<SearchResult> results = new ArrayList<>();
        
        // Create mock search results based on query
        String query = request.getQuery() != null ? request.getQuery().toLowerCase() : "";
        
        if (query.contains("laptop") || query.contains("gaming")) {
            results.add(SearchResult.builder()
                    .id(1L)
                    .name("Gaming Laptop")
                    .description("High-performance gaming laptop with RTX graphics")
                    .sku("LAPTOP-001")
                    .price(BigDecimal.valueOf(1299.99))
                    .imageUrl("/images/gaming-laptop.jpg")
                    .category("Electronics")
                    .inStock(true)
                    .score(0.95)
                    .build());
                    
            results.add(SearchResult.builder()
                    .id(2L)
                    .name("Business Laptop")
                    .description("Professional laptop for business use")
                    .sku("LAPTOP-002")
                    .price(BigDecimal.valueOf(899.99))
                    .imageUrl("/images/business-laptop.jpg")
                    .category("Electronics")
                    .inStock(true)
                    .score(0.85)
                    .build());
        }
        
        if (query.contains("mouse") || query.contains("gaming")) {
            results.add(SearchResult.builder()
                    .id(3L)
                    .name("Gaming Mouse")
                    .description("RGB gaming mouse with high DPI")
                    .sku("MOUSE-001")
                    .price(BigDecimal.valueOf(79.99))
                    .imageUrl("/images/gaming-mouse.jpg")
                    .category("Electronics")
                    .inStock(true)
                    .score(0.90)
                    .build());
        }
        
        if (query.contains("keyboard")) {
            results.add(SearchResult.builder()
                    .id(4L)
                    .name("Mechanical Keyboard")
                    .description("RGB mechanical keyboard with blue switches")
                    .sku("KEYBOARD-001")
                    .price(BigDecimal.valueOf(149.99))
                    .imageUrl("/images/mechanical-keyboard.jpg")
                    .category("Electronics")
                    .inStock(true)
                    .score(0.88)
                    .build());
        }
        
        // Add more mock results for empty or general queries
        if (query.isEmpty() || results.isEmpty()) {
            results.addAll(getDefaultProducts());
        }
        
        return results;
    }
    
    private List<SearchResult> getDefaultProducts() {
        List<SearchResult> products = new ArrayList<>();
        
        products.add(SearchResult.builder()
                .id(5L)
                .name("Wireless Headphones")
                .description("Premium wireless headphones with noise cancellation")
                .sku("HEADPHONES-001")
                .price(BigDecimal.valueOf(199.99))
                .imageUrl("/images/wireless-headphones.jpg")
                .category("Electronics")
                .inStock(true)
                .score(0.92)
                .build());
                
        products.add(SearchResult.builder()
                .id(6L)
                .name("Smartphone")
                .description("Latest smartphone with advanced camera")
                .sku("PHONE-001")
                .price(BigDecimal.valueOf(699.99))
                .imageUrl("/images/smartphone.jpg")
                .category("Electronics")
                .inStock(true)
                .score(0.89)
                .build());
                
        return products;
    }
    

}