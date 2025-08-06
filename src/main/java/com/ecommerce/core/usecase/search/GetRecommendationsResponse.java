package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.recommendation.entity.ProductRecommendation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRecommendationsResponse {
    private List<ProductRecommendation> recommendations;
    private String recommendationType;
    private Integer totalCount;
    private String message;

    public static GetRecommendationsResponse success(List<ProductRecommendation> recommendations, String type) {
        return GetRecommendationsResponse.builder()
                .recommendations(recommendations)
                .recommendationType(type)
                .totalCount(recommendations.size())
                .message("Recommendations retrieved successfully")
                .build();
    }

    public static GetRecommendationsResponse empty(String type) {
        return GetRecommendationsResponse.builder()
                .recommendations(List.of())
                .recommendationType(type)
                .totalCount(0)
                .message("No recommendations available")
                .build();
    }
}