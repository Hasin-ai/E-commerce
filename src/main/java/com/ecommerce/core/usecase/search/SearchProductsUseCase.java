package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.search.entity.SearchCriteria;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProductsUseCase {
    
    private final SearchRepository searchRepository;
    
    public SearchProductsResponse execute(SearchProductsRequest request) {
        SearchCriteria criteria = SearchCriteria.builder()
                .query(request.getQuery())
                .categories(request.getCategories())
                .minPrice(request.getMinPrice())
                .maxPrice(request.getMaxPrice())
                .tags(request.getTags())
                .inStockOnly(request.isInStockOnly())
                .sortBy(request.getSortBy())
                .sortDirection(request.getSortDirection())
                .page(request.getPage())
                .size(request.getSize())
                .build();
        
        var searchResults = searchRepository.search(criteria);
        
        return SearchProductsResponse.builder()
                .results(searchResults.getContent())
                .totalElements(searchResults.getTotalElements())
                .totalPages(searchResults.getTotalPages())
                .currentPage(searchResults.getNumber())
                .size(searchResults.getSize())
                .hasNext(searchResults.hasNext())
                .hasPrevious(searchResults.hasPrevious())
                .build();
    }
}