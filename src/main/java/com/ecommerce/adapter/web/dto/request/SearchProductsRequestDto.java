package com.ecommerce.adapter.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductsRequestDto {
    
    @NotBlank(message = "Search query is required")
    private String query;
    
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
    
    @Min(value = 0, message = "Page must be non-negative")
    @Builder.Default
    private Integer page = 0;
    
    @Min(value = 1, message = "Size must be positive")
    @Builder.Default
    private Integer size = 20;
}