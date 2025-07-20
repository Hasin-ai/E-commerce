package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.search.entity.SearchQuery;
import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Optional;

@Repository
@Slf4j
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "false", matchIfMissing = true)
public class FallbackSearchRepositoryImpl implements SearchRepository {

    @Override
    public Optional<SearchResult> searchProducts(SearchQuery query) {
        log.warn("Elasticsearch is disabled. Search functionality is not available. Returning empty results.");

        SearchResult searchResult = SearchResult.builder()
                .query(query.getQuery())
                .products(Collections.emptyList())
                .totalHits(0)
                .page(query.getPage())
                .pageSize(query.getPageSize())
                .build();

        return Optional.of(searchResult);
    }
}
