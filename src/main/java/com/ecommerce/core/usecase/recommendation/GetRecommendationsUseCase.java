package com.ecommerce.core.usecase.recommendation;

import com.ecommerce.core.domain.recommendation.entity.Recommendation;
import com.ecommerce.core.domain.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetRecommendationsUseCase {

    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> execute(String userId) {
        return recommendationRepository.getRecommendationsForUser(userId);
    }
}
