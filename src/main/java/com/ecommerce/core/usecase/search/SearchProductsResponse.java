package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.search.entity.SearchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductsResponse {
    private List<SearchResult> results;
    private List<SearchResult> products; // Alias for results
    private Integer totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer size;
    private boolean hasNext;
    private boolean hasPrevious;

    public List<SearchResult> getProducts() {
        return results != null ? results : products;
    }
}