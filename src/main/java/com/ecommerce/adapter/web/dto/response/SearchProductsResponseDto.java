package com.ecommerce.adapter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductsResponseDto {
    private List<ProductResponseDto> products;
    private Integer totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private String query;
    private Long searchTime; // in milliseconds
}