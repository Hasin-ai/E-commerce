package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.search.entity.SearchFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductsRequest {
    private String query;
    private SearchFilter filter;
    private List<String> categories;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> tags;
    private boolean inStockOnly;
    private String sortBy;
    private String sortDirection;
    private Integer page;
    private Integer size;

    public static SearchProductsRequest of(String query) {
        return SearchProductsRequest.builder()
                .query(query)
                .page(0)
                .size(20)
                .build();
    }
}