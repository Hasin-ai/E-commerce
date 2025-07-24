package com.ecommerce.core.domain.search.entity;

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
public class SearchCriteria {
    private String query;
    private List<String> categories;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> tags;
    private boolean inStockOnly;
    private String sortBy;
    private String sortDirection;
    private int page;
    private int size;
}