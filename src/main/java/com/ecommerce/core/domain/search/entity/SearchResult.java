package com.ecommerce.core.domain.search.entity;

import com.ecommerce.core.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
    private String query;
    private List<Product> products;
    private int totalHits;
    private int page;
    private int pageSize;
}
