package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchProductsUseCase {
    
    private final SearchRepository searchRepository;
    
    public SearchProductsResponse execute(SearchProductsRequest request) {
        List<SearchResult> results = searchRepository.searchProducts(request);
        int totalCount = searchRepository.countSearchResults(request);
        
        int totalPages = (int) Math.ceil((double) totalCount / request.getSize());
        
        return SearchProductsResponse.builder()
                .results(results)
                .products(results)
                .totalElements(totalCount)
                .totalPages(totalPages)
                .currentPage(request.getPage())
                .size(request.getSize())
                .hasNext(request.getPage() < totalPages - 1)
                .hasPrevious(request.getPage() > 0)
                .build();
    }
}