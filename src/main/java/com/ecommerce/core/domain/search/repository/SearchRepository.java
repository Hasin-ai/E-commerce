package com.ecommerce.core.domain.search.repository;

import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.usecase.search.SearchProductsRequest;

import java.util.List;

public interface SearchRepository {
    List<SearchResult> searchProducts(SearchProductsRequest request);
    int countSearchResults(SearchProductsRequest request);
}