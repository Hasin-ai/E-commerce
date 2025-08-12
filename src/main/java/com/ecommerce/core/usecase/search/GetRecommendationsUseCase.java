package com.ecommerce.core.usecase.search;

import com.ecommerce.core.domain.recommendation.entity.ProductRecommendation;
import com.ecommerce.core.domain.recommendation.entity.RecommendationType;
import com.ecommerce.core.domain.recommendation.repository.RecommendationRepository;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRecommendationsUseCase {
    
    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;

    public GetRecommendationsUseCase(RecommendationRepository recommendationRepository,
                                   UserRepository userRepository) {
        this.recommendationRepository = recommendationRepository;
        this.userRepository = userRepository;
    }

    public GetRecommendationsResponse execute(GetRecommendationsRequest request) {
        // If user-specific recommendations
        if (request.getUserId() != null) {
            validateUser(request.getUserId());
            return getUserRecommendations(request);
        }
        
        // If product-specific recommendations
        if (request.getProductId() != null) {
            return getProductRecommendations(request);
        }
        
        // Default to trending products
        return getTrendingRecommendations(request);
    }

    private GetRecommendationsResponse getUserRecommendations(GetRecommendationsRequest request) {
        List<ProductRecommendation> recommendations = recommendationRepository
                .findPersonalizedRecommendations(
                    request.getUserId(), 
                    request.getLimit() != null ? request.getLimit() : 10
                );
        
        return GetRecommendationsResponse.success(recommendations, "PERSONALIZED");
    }

    private GetRecommendationsResponse getProductRecommendations(GetRecommendationsRequest request) {
        RecommendationType type = request.getType() != null ? 
            request.getType() : RecommendationType.SIMILAR_PRODUCTS;
            
        List<ProductRecommendation> recommendations = recommendationRepository
                .findSimilarProducts(
                    request.getProductId(), 
                    type,
                    request.getLimit() != null ? request.getLimit() : 10
                );
        
        return GetRecommendationsResponse.success(recommendations, type.name());
    }

    private GetRecommendationsResponse getTrendingRecommendations(GetRecommendationsRequest request) {
        List<ProductRecommendation> recommendations = recommendationRepository
                .findTrendingProducts(
                    request.getCategory(),
                    request.getLimit() != null ? request.getLimit() : 10
                );
        
        return GetRecommendationsResponse.success(recommendations, "TRENDING");
    }

    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with id: " + userId);
        }
    }
}