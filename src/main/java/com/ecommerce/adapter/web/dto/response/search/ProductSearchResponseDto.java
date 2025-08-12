package com.ecommerce.adapter.web.dto.response.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import java.util.List;
import java.util.Map;

public class ProductSearchResponseDto {

    @JsonProperty("products")
    private List<ProductResponseDto> products;

    @JsonProperty("totalElements")
    private Long totalElements;

    @JsonProperty("totalPages")
    private Integer totalPages;

    @JsonProperty("currentPage")
    private Integer currentPage;

    @JsonProperty("pageSize")
    private Integer pageSize;

    @JsonProperty("hasNext")
    private Boolean hasNext;

    @JsonProperty("hasPrevious")
    private Boolean hasPrevious;

    @JsonProperty("searchQuery")
    private String searchQuery;

    @JsonProperty("appliedFilters")
    private Map<String, Object> appliedFilters;

    @JsonProperty("availableFilters")
    private Map<String, List<String>> availableFilters; // Filter options like brands, categories, etc.

    @JsonProperty("suggestions")
    private List<String> suggestions; // Search suggestions

    @JsonProperty("executionTimeMs")
    private Long executionTimeMs;

    // Default constructor
    public ProductSearchResponseDto() {}

    // Constructor
    public ProductSearchResponseDto(List<ProductResponseDto> products, Long totalElements,
                                   Integer totalPages, Integer currentPage, Integer pageSize,
                                   Boolean hasNext, Boolean hasPrevious, String searchQuery,
                                   Map<String, Object> appliedFilters, Map<String, List<String>> availableFilters,
                                   List<String> suggestions, Long executionTimeMs) {
        this.products = products;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
        this.searchQuery = searchQuery;
        this.appliedFilters = appliedFilters;
        this.availableFilters = availableFilters;
        this.suggestions = suggestions;
        this.executionTimeMs = executionTimeMs;
    }

    // Getters and Setters
    public List<ProductResponseDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponseDto> products) {
        this.products = products;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Map<String, Object> getAppliedFilters() {
        return appliedFilters;
    }

    public void setAppliedFilters(Map<String, Object> appliedFilters) {
        this.appliedFilters = appliedFilters;
    }

    public Map<String, List<String>> getAvailableFilters() {
        return availableFilters;
    }

    public void setAvailableFilters(Map<String, List<String>> availableFilters) {
        this.availableFilters = availableFilters;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
}
