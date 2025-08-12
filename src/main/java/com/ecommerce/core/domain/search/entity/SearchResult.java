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
public class SearchResult {
    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private Double score;
    private List<String> tags;
    private boolean inStock;
}