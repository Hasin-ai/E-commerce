package com.ecommerce.adapter.web.dto.response.search;

import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {
    private String query;
    private List<ProductResponseDto> products;
    private int totalHits;
    private int page;
    private int pageSize;
}