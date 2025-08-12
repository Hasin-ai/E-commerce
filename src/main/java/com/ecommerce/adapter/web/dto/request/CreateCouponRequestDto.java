package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateCouponRequestDto {

    @JsonProperty("code")
    @NotBlank(message = "Coupon code is required")
    @Size(min = 3, max = 50, message = "Coupon code must be between 3 and 50 characters")
    private String code;

    @JsonProperty("name")
    @NotBlank(message = "Coupon name is required")
    @Size(max = 100, message = "Coupon name cannot exceed 100 characters")
    private String name;

    @JsonProperty("description")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @JsonProperty("discountType")
    @NotBlank(message = "Discount type is required")
    private String discountType; // PERCENTAGE, FIXED_AMOUNT, FREE_SHIPPING

    @JsonProperty("discountValue")
    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    @JsonProperty("minimumOrderAmount")
    @DecimalMin(value = "0", message = "Minimum order amount cannot be negative")
    private BigDecimal minimumOrderAmount;

    @JsonProperty("maximumDiscountAmount")
    private BigDecimal maximumDiscountAmount;

    @JsonProperty("usageLimit")
    private Integer usageLimit; // Total usage limit

    @JsonProperty("usageLimitPerUser")
    private Integer usageLimitPerUser;

    @JsonProperty("validFrom")
    @NotNull(message = "Valid from date is required")
    private LocalDateTime validFrom;

    @JsonProperty("validUntil")
    @NotNull(message = "Valid until date is required")
    private LocalDateTime validUntil;

    @JsonProperty("isActive")
    private Boolean isActive = true;

    @JsonProperty("applicableProductIds")
    private java.util.List<Long> applicableProductIds;

    @JsonProperty("applicableCategoryIds")
    private java.util.List<Long> applicableCategoryIds;

    @JsonProperty("excludedProductIds")
    private java.util.List<Long> excludedProductIds;

    // Default constructor
    public CreateCouponRequestDto() {}

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public BigDecimal getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(BigDecimal maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getUsageLimitPerUser() {
        return usageLimitPerUser;
    }

    public void setUsageLimitPerUser(Integer usageLimitPerUser) {
        this.usageLimitPerUser = usageLimitPerUser;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public java.util.List<Long> getApplicableProductIds() {
        return applicableProductIds;
    }

    public void setApplicableProductIds(java.util.List<Long> applicableProductIds) {
        this.applicableProductIds = applicableProductIds;
    }

    public java.util.List<Long> getApplicableCategoryIds() {
        return applicableCategoryIds;
    }

    public void setApplicableCategoryIds(java.util.List<Long> applicableCategoryIds) {
        this.applicableCategoryIds = applicableCategoryIds;
    }

    public java.util.List<Long> getExcludedProductIds() {
        return excludedProductIds;
    }

    public void setExcludedProductIds(java.util.List<Long> excludedProductIds) {
        this.excludedProductIds = excludedProductIds;
    }
}
