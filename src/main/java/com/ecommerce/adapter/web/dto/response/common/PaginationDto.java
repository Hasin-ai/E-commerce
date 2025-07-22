package com.ecommerce.adapter.web.dto.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaginationDto {

    @JsonProperty("currentPage")
    private Integer currentPage;

    @JsonProperty("pageSize")
    private Integer pageSize;

    @JsonProperty("totalElements")
    private Long totalElements;

    @JsonProperty("totalPages")
    private Integer totalPages;

    @JsonProperty("hasNext")
    private Boolean hasNext;

    @JsonProperty("hasPrevious")
    private Boolean hasPrevious;

    @JsonProperty("isFirst")
    private Boolean isFirst;

    @JsonProperty("isLast")
    private Boolean isLast;

    // Default constructor
    public PaginationDto() {}

    // Constructor
    public PaginationDto(Integer currentPage, Integer pageSize, Long totalElements, Integer totalPages) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = currentPage < totalPages - 1;
        this.hasPrevious = currentPage > 0;
        this.isFirst = currentPage == 0;
        this.isLast = currentPage == totalPages - 1;
    }

    // Getters and Setters
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

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(Boolean isLast) {
        this.isLast = isLast;
    }
}
