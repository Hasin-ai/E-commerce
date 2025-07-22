package com.ecommerce.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CouponResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("discountType")
    private String discountType;

    @JsonProperty("discountValue")
    private BigDecimal discountValue;

    @JsonProperty("minimumOrderAmount")
    private BigDecimal minimumOrderAmount;

    @JsonProperty("maximumDiscountAmount")
    private BigDecimal maximumDiscountAmount;

    @JsonProperty("usageLimit")
    private Integer usageLimit;

    @JsonProperty("usageLimitPerUser")
    private Integer usageLimitPerUser;

    @JsonProperty("currentUsageCount")
    private Integer currentUsageCount;

    @JsonProperty("validFrom")
    private LocalDateTime validFrom;

    @JsonProperty("validUntil")
    private LocalDateTime validUntil;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("isExpired")
    private Boolean isExpired;

    @JsonProperty("applicableProductIds")
    private List<Long> applicableProductIds;

    @JsonProperty("applicableCategoryIds")
    private List<Long> applicableCategoryIds;

    @JsonProperty("excludedProductIds")
    private List<Long> excludedProductIds;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    // Default constructor
    public CouponResponseDto() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getCurrentUsageCount() {
        return currentUsageCount;
    }

    public void setCurrentUsageCount(Integer currentUsageCount) {
        this.currentUsageCount = currentUsageCount;
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

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public List<Long> getApplicableProductIds() {
        return applicableProductIds;
    }

    public void setApplicableProductIds(List<Long> applicableProductIds) {
        this.applicableProductIds = applicableProductIds;
    }

    public List<Long> getApplicableCategoryIds() {
        return applicableCategoryIds;
    }

    public void setApplicableCategoryIds(List<Long> applicableCategoryIds) {
        this.applicableCategoryIds = applicableCategoryIds;
    }

    public List<Long> getExcludedProductIds() {
        return excludedProductIds;
    }

    public void setExcludedProductIds(List<Long> excludedProductIds) {
        this.excludedProductIds = excludedProductIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
