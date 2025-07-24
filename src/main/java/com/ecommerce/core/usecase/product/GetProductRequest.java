package com.ecommerce.core.usecase.product;

public class GetProductRequest {
    private Long productId;

    public GetProductRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}