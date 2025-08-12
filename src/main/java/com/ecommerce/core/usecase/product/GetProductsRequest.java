package com.ecommerce.core.usecase.product;

public class GetProductsRequest {
    private Long categoryId;
    private String search;
    private Boolean featured;

    public GetProductsRequest(Long categoryId, String search, Boolean featured) {
        this.categoryId = categoryId;
        this.search = search;
        this.featured = featured;
    }

    // Getters
    public Long getCategoryId() { return categoryId; }
    public String getSearch() { return search; }
    public Boolean getFeatured() { return featured; }
}