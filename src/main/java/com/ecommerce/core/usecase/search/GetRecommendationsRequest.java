package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.recommendation.entity.RecommendationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRecommendationsRequest {
    private Long userId;
    private Long productId; // For product-specific recommendations
    private RecommendationType type;
    private Integer limit;
    private String category;

    public static GetRecommendationsRequest forUser(Long userId, Integer limit) {
        return GetRecommendationsRequest.builder()
                .userId(userId)
                .limit(limit)
                .build();
    }

    public static GetRecommendationsRequest forProduct(Long productId, RecommendationType type, Integer limit) {
        return GetRecommendationsRequest.builder()
                .productId(productId)
                .type(type)
                .limit(limit)
                .build();
    }
}