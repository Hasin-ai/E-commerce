package com.ecommerce.core.domain.recommendation.entity;

import java.time.LocalDateTime;

public class Recommendation {
    private Long id;
    private Long userId;
    private Long productId;
    private String recommendationType; // "viewed_together", "bought_together", "similar", "trending"
    private double score;
    private String reason;
    private LocalDateTime createdAt;

    public Recommendation(Long userId, Long productId, String recommendationType, double score, String reason) {
        this.userId = userId;
        this.productId = productId;
        this.recommendationType = recommendationType;
        this.score = score;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getProductId() { return productId; }
    public String getRecommendationType() { return recommendationType; }
    public double getScore() { return score; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }
}
