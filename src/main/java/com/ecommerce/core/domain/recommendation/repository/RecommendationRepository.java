package com.ecommerce.core.domain.recommendation.repository;

import com.ecommerce.core.domain.recommendation.entity.Recommendation;
import java.util.List;

public interface RecommendationRepository {
    Recommendation save(Recommendation recommendation);
    List<Recommendation> findByUserId(Long userId);
    List<Recommendation> findByUserIdAndType(Long userId, String recommendationType);
    List<Recommendation> findTopRecommendations(Long userId, int limit);
    void deleteByUserId(Long userId);
}
