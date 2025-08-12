package com.ecommerce.core.domain.search.entity;

import java.time.LocalDateTime;
import java.util.List;

public class SearchQuery {
    private Long id;
    private Long userId;
    private String query;
    private String category;
    private List<String> filters;
    private int resultsCount;
    private LocalDateTime searchedAt;

    public SearchQuery(Long userId, String query, String category, List<String> filters) {
        this.userId = userId;
        this.query = query.toLowerCase().trim();
        this.category = category;
        this.filters = filters;
        this.searchedAt = LocalDateTime.now();
    }

    public static SearchQueryBuilder builder() {
        return new SearchQueryBuilder();
    }

    public void setResultsCount(int count) {
        this.resultsCount = count;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getQuery() { return query; }
    public String getCategory() { return category; }
    public List<String> getFilters() { return filters; }
    public int getResultsCount() { return resultsCount; }
    public LocalDateTime getSearchedAt() { return searchedAt; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }

    public static class SearchQueryBuilder {
        private Long userId;
        private String query;
        private String category;
        private List<String> filters;
        private int page;
        private int pageSize;
        private String sortBy;
        private String sortOrder;
        private LocalDateTime timestamp;

        public SearchQueryBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public SearchQueryBuilder query(String query) {
            this.query = query;
            return this;
        }

        public SearchQueryBuilder category(String category) {
            this.category = category;
            return this;
        }

        public SearchQueryBuilder filters(List<String> filters) {
            this.filters = filters;
            return this;
        }

        public SearchQueryBuilder page(int page) {
            this.page = page;
            return this;
        }

        public SearchQueryBuilder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public SearchQueryBuilder sortBy(String sortBy) {
            this.sortBy = sortBy;
            return this;
        }

        public SearchQueryBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public SearchQueryBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public SearchQuery build() {
            SearchQuery searchQuery = new SearchQuery(userId, query, category, filters);
            // Set additional properties if needed
            return searchQuery;
        }
    }
}
