package com.ecommerce.adapter.web.dto.request.search;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.Map;

@Data
public class SearchRequestDto {
    @NotEmpty(message = "Search query cannot be empty")
    private String query;
    
    @Min(value = 0, message = "Page number must be non-negative")
    private int page = 0;
    
    @Min(value = 1, message = "Page size must be at least 1")
    private int pageSize = 10;

    private Map<String, String> filters;
    private String sortBy;
    private String sortOrder;
}
