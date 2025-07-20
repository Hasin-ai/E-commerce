package com.ecommerce.core.domain.search.repository;

import com.ecommerce.core.domain.search.entity.SearchQuery;
import com.ecommerce.core.domain.search.entity.SearchResult;
import java.util.Optional;

public interface SearchRepository {
    Optional<SearchResult> searchProducts(SearchQuery query);
}
