package com.ecommerce.core.domain.search.entity;

import com.ecommerce.core.domain.product.entity.Product;
import java.util.List;

public class SearchResult {
    private List<Product> products;
    private long totalCount;
    private int page;
    private int pageSize;
    private String query;
    private long searchTimeMs;

    public SearchResult(List<Product> products, long totalCount, int page, int pageSize, String query, long searchTimeMs) {
        this.products = products;
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
        this.query = query;
        this.searchTimeMs = searchTimeMs;
    }

    public static SearchResultBuilder builder() {
        return new SearchResultBuilder();
    }

    // Getters
    public List<Product> getProducts() { return products; }
    public long getTotalCount() { return totalCount; }
    public int getPage() { return page; }
    public int getPageSize() { return pageSize; }
    public String getQuery() { return query; }
    public long getSearchTimeMs() { return searchTimeMs; }

    public static class SearchResultBuilder {
        private List<Product> products;
        private long totalCount;
        private int page;
        private int pageSize;
        private String query;
        private long searchTimeMs;

        public SearchResultBuilder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public SearchResultBuilder totalCount(long totalCount) {
            this.totalCount = totalCount;
            return this;
        }

        public SearchResultBuilder page(int page) {
            this.page = page;
            return this;
        }

        public SearchResultBuilder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public SearchResultBuilder query(String query) {
            this.query = query;
            return this;
        }

        public SearchResultBuilder searchTime(long searchTimeMs) {
            this.searchTimeMs = searchTimeMs;
            return this;
        }

        public SearchResultBuilder totalPages(int totalPages) {
            // Calculate total pages from totalCount and pageSize
            if (pageSize > 0) {
                this.pageSize = pageSize;
            }
            return this;
        }

        public SearchResult build() {
            return new SearchResult(products, totalCount, page, pageSize, query, searchTimeMs);
        }
    }

    // Additional getter methods needed by SearchProductUseCase
    public int getTotalPages() {
        return pageSize > 0 ? (int) Math.ceil((double) totalCount / pageSize) : 0;
    }

    public long getSearchTime() {
        return searchTimeMs;
    }
}
