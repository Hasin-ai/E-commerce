package com.ecommerce.core.domain.recommendation.entity;

public enum RecommendationType {
    VIEWED_TOGETHER("Customers who viewed this item also viewed"),
    BOUGHT_TOGETHER("Frequently bought together"),
    SIMILAR_PRODUCTS("Similar products"),
    TRENDING("Trending now"),
    PERSONALIZED("Recommended for you"),
    RECENTLY_VIEWED("Recently viewed"),
    CATEGORY_BASED("More in this category"),
    PRICE_BASED("Similar price range");

    private final String displayName;

    RecommendationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}