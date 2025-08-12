package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class AddCartItemRequestDto {

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    @JsonProperty("productId")
    private Long productId;

    // Product name and price will be fetched automatically based on productId
    // These fields are not required in the request

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Constructors
    public AddCartItemRequestDto() {}

    // Getters and setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    // Removed productName and unitPrice getters/setters as they're not needed

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}