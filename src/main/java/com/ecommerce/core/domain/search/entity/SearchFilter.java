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
public class SearchFilter {
    private String category;
    private List<String> brands;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<String> colors;
    private List<String> sizes;
    private Boolean inStock;
    private Boolean featured;
    private Integer minRating;
    private String sortBy; // price, name, rating, popularity
    private String sortDirection; // asc, desc

    public boolean hasFilters() {
        return category != null || 
               (brands != null && !brands.isEmpty()) ||
               minPrice != null || 
               maxPrice != null ||
               (colors != null && !colors.isEmpty()) ||
               (sizes != null && !sizes.isEmpty()) ||
               inStock != null ||
               featured != null ||
               minRating != null;
    }
}