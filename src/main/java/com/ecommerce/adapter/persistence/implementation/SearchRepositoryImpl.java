package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.elasticsearch.repository.ProductSearchRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.search.entity.SearchQuery;
import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
public class SearchRepositoryImpl implements SearchRepository {

    private final ProductSearchRepository productSearchRepository;

    @Override
    public Optional<SearchResult> searchProducts(SearchQuery query) {
        int page = (query.getPage() > 0) ? query.getPage() : query.getDefaultPage();
        int pageSize = (query.getPageSize() > 0) ? query.getPageSize() : query.getDefaultPageSize();
        
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Product> productPage = productSearchRepository.search(query.getQuery(), pageRequest);

        SearchResult searchResult = SearchResult.builder()
                .query(query.getQuery())
                .products(productPage.getContent())
                .totalHits((int) productPage.getTotalElements())
                .page(productPage.getNumber())
                .pageSize(productPage.getSize())
                .build();

        return Optional.of(searchResult);
    }
}