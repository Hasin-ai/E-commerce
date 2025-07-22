package com.ecommerce.core.domain.search.repository;

import com.ecommerce.core.domain.search.entity.SearchQuery;
import com.ecommerce.core.domain.search.entity.SearchResult;
import java.util.List;

public interface SearchRepository {
    SearchQuery save(SearchQuery searchQuery);
    List<SearchQuery> findByUserId(Long userId);
    List<String> findMostSearchedQueries(int limit);
    List<SearchQuery> findRecentSearches(Long userId, int limit);

    SearchResult searchProducts(SearchQuery searchQuery);
    void saveSearchQuery(SearchQuery searchQuery);
    List<String> getSearchSuggestions(String query, int limit);
}
