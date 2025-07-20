package com.ecommerce.core.domain.recommendation.entity;

import com.ecommerce.core.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {
    private Product product;
    private double score;
}
