package com.ecommerce.adapter.web.dto.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class GetRecommendationsRequestDto {

    @JsonProperty("userId")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("productId")
    private Long productId; // For product-based recommendations

    @JsonProperty("categoryId")
    private Long categoryId; // For category-based recommendations

    @JsonProperty("recommendationType")
    private String recommendationType; // SIMILAR_PRODUCTS, FREQUENTLY_BOUGHT_TOGETHER, PERSONALIZED, TRENDING

    @JsonProperty("limit")
    private Integer limit = 10;

    @JsonProperty("excludeOutOfStock")
    private Boolean excludeOutOfStock = true;

    // Default constructor
    public GetRecommendationsRequestDto() {}

    // Constructor
    public GetRecommendationsRequestDto(Long userId, Long productId, Long categoryId,
                                      String recommendationType, Integer limit, Boolean excludeOutOfStock) {
        this.userId = userId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.recommendationType = recommendationType;
        this.limit = limit;
        this.excludeOutOfStock = excludeOutOfStock;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getExcludeOutOfStock() {
        return excludeOutOfStock;
    }

    public void setExcludeOutOfStock(Boolean excludeOutOfStock) {
        this.excludeOutOfStock = excludeOutOfStock;
    }
}
