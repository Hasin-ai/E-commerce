package com.ecommerce.adapter.web.dto.request.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ProductSearchRequestDto {

    @JsonProperty("query")
    @Size(max = 255, message = "Search query cannot exceed 255 characters")
    private String query;

    @JsonProperty("categoryId")
    private Long categoryId;

    @JsonProperty("minPrice")
    @Min(value = 0, message = "Minimum price cannot be negative")
    private BigDecimal minPrice;

    @JsonProperty("maxPrice")
    @Min(value = 0, message = "Maximum price cannot be negative")
    private BigDecimal maxPrice;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("inStock")
    private Boolean inStock;

    @JsonProperty("sortBy")
    private String sortBy; // PRICE_ASC, PRICE_DESC, NAME_ASC, NAME_DESC, POPULARITY, RATING

    @JsonProperty("page")
    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;

    @JsonProperty("size")
    @Min(value = 1, message = "Page size must be at least 1")
    private Integer size = 20;

    @JsonProperty("filters")
    private java.util.Map<String, Object> filters; // Additional custom filters

    // Default constructor
    public ProductSearchRequestDto() {}

    // Getters and Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public java.util.Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(java.util.Map<String, Object> filters) {
        this.filters = filters;
    }
}
