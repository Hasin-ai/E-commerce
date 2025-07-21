package com.ecommerce.adapter.web.dto.response;

import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDto {
    private ProductResponseDto product;
    private double score;
}
