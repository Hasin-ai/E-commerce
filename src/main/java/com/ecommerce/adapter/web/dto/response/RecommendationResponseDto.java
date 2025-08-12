package com.ecommerce.adapter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseDto {
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal productPrice;
    private String recommendationType;
    private Double score;
    private String reason;
}