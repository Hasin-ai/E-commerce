package com.ecommerce.core.domain.search.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuery {
    private String query;
    private String userId;
    private LocalDateTime timestamp;
    private int resultCount;
    private int page;
    private int pageSize;
    
    @Builder.Default
    private int defaultPage = 0;
    
    @Builder.Default
    private int defaultPageSize = 10;
}
