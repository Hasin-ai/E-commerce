package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateProductRequestDto {

    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private String name;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String slug;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Pattern(regexp = "^[A-Z0-9-]{3,20}$", message = "SKU must be 3-20 characters, alphanumeric and hyphens only")
    private String sku;

    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be positive")
    @Digits(integer = 10, fraction = 2, message = "Base price must have at most 2 decimal places")
    @JsonProperty("basePrice")
    private BigDecimal basePrice;

    @DecimalMin(value = "0.0", inclusive = false, message = "Sale price must be positive")
    @Digits(integer = 10, fraction = 2, message = "Sale price must have at most 2 decimal places")
    @JsonProperty("salePrice")
    private BigDecimal salePrice;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a valid 3-letter code")
    private String currency;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("isFeatured")
    private Boolean isFeatured;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }

    public Boolean getIsFeatured() { return isFeatured; }
    public void setFeatured(Boolean featured) { isFeatured = featured; }
}
