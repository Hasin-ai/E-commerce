package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.search.entity.SearchQuery;
import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SearchProductUseCase {

    private final SearchRepository searchRepository;

    public SearchResult execute(String query, String userId, int page, int pageSize) {
        SearchQuery searchQuery = SearchQuery.builder()
                .query(query)
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .page(page)
                .pageSize(pageSize)
                .build();

        return searchRepository.searchProducts(searchQuery)
                .orElseThrow(() -> new ResourceNotFoundException("No products found for query: " + query));
    }
    
    // Overloaded method for backward compatibility
    public SearchResult execute(String query, String userId) {
        return execute(query, userId, 0, 10);
    }
}
