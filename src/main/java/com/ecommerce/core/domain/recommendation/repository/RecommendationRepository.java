package com.ecommerce.core.domain.recommendation.repository;

import com.ecommerce.core.domain.recommendation.entity.Recommendation;

import java.util.List;

public interface RecommendationRepository {
    List<Recommendation> getRecommendationsForUser(String userId);
}
