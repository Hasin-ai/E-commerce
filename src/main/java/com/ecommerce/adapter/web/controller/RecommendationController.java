package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.response.RecommendationDto;
import com.ecommerce.adapter.web.mapper.RecommendationMapper;
import com.ecommerce.core.domain.recommendation.entity.Recommendation;
import com.ecommerce.core.usecase.recommendation.GetRecommendationsUseCase;
import com.ecommerce.infrastructure.security.CustomUserDetails;
import com.ecommerce.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final GetRecommendationsUseCase getRecommendationsUseCase;
    private final RecommendationMapper recommendationMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecommendationDto>>> getRecommendations(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        String userId = (currentUser != null) ? currentUser.getId().toString() : null;
        List<Recommendation> recommendations = getRecommendationsUseCase.execute(userId);
        List<RecommendationDto> recommendationDtos = recommendations.stream()
                .map(recommendationMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, recommendationDtos, "Recommendations found"));
    }
}
