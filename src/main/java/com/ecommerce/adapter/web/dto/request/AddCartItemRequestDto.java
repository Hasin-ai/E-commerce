package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class AddCartItemRequestDto {

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    @JsonProperty("productId")
    private Long productId;

    @NotBlank(message = "Product name is required")
    @JsonProperty("productName")
    private String productName;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be positive")
    @Digits(integer = 10, fraction = 2, message = "Unit price must have at most 2 decimal places")
    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Constructors
    public AddCartItemRequestDto() {}

    // Getters and setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}