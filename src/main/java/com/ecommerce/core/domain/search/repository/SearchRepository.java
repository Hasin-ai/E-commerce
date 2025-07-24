package com.ecommerce.core.domain.search.repository;

import com.ecommerce.core.domain.search.entity.SearchCriteria;
import com.ecommerce.core.domain.search.entity.SearchResult;
import org.springframework.data.domain.Page;

public interface SearchRepository {
    Page<SearchResult> search(SearchCriteria criteria);
    void indexProduct(Long productId);
    void deleteProduct(Long productId);
    void reindexAll();
}