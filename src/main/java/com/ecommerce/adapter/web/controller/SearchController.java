package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.SearchProductsRequestDto;
import com.ecommerce.adapter.web.dto.request.TrackEventRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.adapter.web.dto.response.RecommendationDto;
import com.ecommerce.adapter.web.dto.response.RecommendationResponseDto;
import com.ecommerce.adapter.web.dto.response.SearchProductsResponseDto;
import com.ecommerce.adapter.web.dto.response.SearchResponse;
import com.ecommerce.core.usecase.search.SearchProductsUseCase;
import com.ecommerce.core.usecase.search.SearchProductsRequest;
import com.ecommerce.core.usecase.search.SearchProductsResponse;
import com.ecommerce.core.usecase.search.GetRecommendationsUseCase;
import com.ecommerce.core.usecase.search.GetRecommendationsRequest;
import com.ecommerce.core.usecase.search.GetRecommendationsResponse;
import com.ecommerce.core.usecase.analytics.TrackEventUseCase;
import com.ecommerce.core.usecase.analytics.TrackEventRequest;
import com.ecommerce.core.domain.search.entity.SearchFilter;
import com.ecommerce.shared.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@Validated
@RequiredArgsConstructor
public class SearchController {

    private final SearchProductsUseCase searchProductsUseCase;
    private final GetRecommendationsUseCase getRecommendationsUseCase;
    private final TrackEventUseCase trackEventUseCase;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<SearchProductsResponseDto>> searchProducts(
            @RequestParam @NotBlank String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            HttpServletRequest request,
            Authentication authentication) {
        
        long startTime = System.currentTimeMillis();
        
        SearchFilter filter = SearchFilter.builder()
                .category(category)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        
        SearchProductsRequest searchRequest = SearchProductsRequest.builder()
                .query(query)
                .filter(filter)
                .page(page)
                .size(size)
                .build();
        
        SearchProductsResponse response = searchProductsUseCase.execute(searchRequest);
        
        // Track search event
        if (authentication != null) {
            trackSearchEvent(query, response.getTotalElements(), request, authentication);
        }
        
        SearchProductsResponseDto responseDto = SearchProductsResponseDto.builder()
                .products(response.getProducts().stream()
                    .map(this::mapToProductResponseDto)
                    .collect(Collectors.toList()))
                .totalElements(response.getTotalElements())
                .totalPages(response.getTotalPages())
                .currentPage(page)
                .pageSize(size)
                .query(query)
                .searchTime(System.currentTimeMillis() - startTime)
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(responseDto, "Search results retrieved successfully"));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse<List<SearchResponse>>> getSearchSuggestions(
            @RequestParam @NotBlank String query,
            @RequestParam(defaultValue = "10") int limit) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Search suggestions retrieved successfully"));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<RecommendationResponseDto>>> getRecommendations(
            Authentication authentication,
            @RequestParam(defaultValue = "10") int limit) {
        
        if (authentication == null) {
            // Return trending products for anonymous users
            GetRecommendationsRequest request = GetRecommendationsRequest.builder()
                    .limit(limit)
                    .build();
            
            GetRecommendationsResponse response = getRecommendationsUseCase.execute(request);
            
            List<RecommendationResponseDto> recommendations = response.getRecommendations().stream()
                    .map(this::mapToRecommendationResponseDto)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(recommendations, "Trending recommendations retrieved successfully"));
        }
        
        // Get user ID from authentication (you'll need to implement this based on your auth system)
        Long userId = getUserIdFromAuthentication(authentication);
        
        GetRecommendationsRequest request = GetRecommendationsRequest.forUser(userId, limit);
        GetRecommendationsResponse response = getRecommendationsUseCase.execute(request);
        
        List<RecommendationResponseDto> recommendations = response.getRecommendations().stream()
                .map(this::mapToRecommendationResponseDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(recommendations, "Personalized recommendations retrieved successfully"));
    }

    @GetMapping("/recommendations/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getSimilarProducts(
            @PathVariable @NotNull @Positive Long productId,
            @RequestParam(defaultValue = "5") int limit) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Similar products retrieved successfully"));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getPopularProducts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") int limit) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Popular products retrieved successfully"));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getTrendingProducts(
            @RequestParam(defaultValue = "10") int limit) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Trending products retrieved successfully"));
    }

    @PostMapping("/track")
    public ResponseEntity<ApiResponse<Void>> trackSearchQuery(
            @RequestParam @NotBlank String query,
            @RequestParam(required = false) Integer resultCount,
            Authentication authentication) {
        
        String userEmail = authentication != null ? authentication.getName() : null;
        
        // TODO: Implement search tracking
        return ResponseEntity.ok(ApiResponse.success(null, "Search query tracked successfully"));
    }

    // Helper methods
    private void trackSearchEvent(String query, Integer resultCount, HttpServletRequest request, Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            
            TrackEventRequest trackRequest = TrackEventRequest.builder()
                    .eventType("SEARCH")
                    .userId(userId)
                    .properties(java.util.Map.of(
                        "query", query,
                        "result_count", resultCount
                    ))
                    .userAgent(request.getHeader("User-Agent"))
                    .ipAddress(request.getRemoteAddr())
                    .build();
            
            trackEventUseCase.execute(trackRequest);
        } catch (Exception e) {
            // Log error but don't fail the search
            System.err.println("Failed to track search event: " + e.getMessage());
        }
    }
    
    private Long getUserIdFromAuthentication(Authentication authentication) {
        // This is a placeholder - implement based on your authentication system
        // You might need to cast to your custom UserDetails or extract from JWT
        return 1L; // TODO: Implement proper user ID extraction
    }
    
    private ProductResponseDto mapToProductResponseDto(com.ecommerce.core.domain.search.entity.SearchResult searchResult) {
        return ProductResponseDto.builder()
                .id(searchResult.getId())
                .name(searchResult.getName())
                .description(searchResult.getDescription())
                .sku(searchResult.getSku())
                .price(searchResult.getPrice())
                .imageUrl(searchResult.getImageUrl())
                .category(searchResult.getCategory())
                .inStock(searchResult.isInStock())
                .build();
    }
    
    private RecommendationResponseDto mapToRecommendationResponseDto(com.ecommerce.core.domain.recommendation.entity.ProductRecommendation recommendation) {
        return RecommendationResponseDto.builder()
                .productId(recommendation.getProductId())
                .productName(recommendation.getProductName())
                .productImageUrl(recommendation.getProductImageUrl())
                .productPrice(recommendation.getProductPrice())
                .recommendationType(recommendation.getType().name())
                .score(recommendation.getScore())
                .reason(recommendation.getReason())
                .build();
    }
}