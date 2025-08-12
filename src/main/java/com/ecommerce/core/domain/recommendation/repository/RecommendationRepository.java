package com.ecommerce.core.domain.recommendation.repository;

import com.ecommerce.core.domain.recommendation.entity.ProductRecommendation;
import com.ecommerce.core.domain.recommendation.entity.RecommendationType;

import java.util.List;

public interface RecommendationRepository {
    
    List<ProductRecommendation> findPersonalizedRecommendations(Long userId, int limit);
    
    List<ProductRecommendation> findSimilarProducts(Long productId, RecommendationType type, int limit);
    
    List<ProductRecommendation> findTrendingProducts(String category, int limit);
    
    List<ProductRecommendation> findRecommendationsByType(Long userId, RecommendationType type, int limit);
    
    void saveRecommendation(ProductRecommendation recommendation);
    
    void deleteExpiredRecommendations();
    
    List<ProductRecommendation> findViewedTogether(Long productId, int limit);
    
    List<ProductRecommendation> findBoughtTogether(Long productId, int limit);
}