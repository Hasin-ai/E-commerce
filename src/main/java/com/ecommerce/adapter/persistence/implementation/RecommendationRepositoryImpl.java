package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.recommendation.entity.Recommendation;
import com.ecommerce.core.domain.recommendation.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RecommendationRepositoryImpl implements RecommendationRepository {

    private final ProductRepository productRepository;

    @Override
    public List<Recommendation> getRecommendationsForUser(String userId) {
        // This is a simplified implementation. A real-world implementation would
        // use a recommendation engine or a more complex algorithm.
        return productRepository.findFeaturedProducts()
                .stream()
                .map(product -> new Recommendation(product, 0.9))
                .collect(Collectors.toList());
    }
}