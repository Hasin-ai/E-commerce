package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.entity.ProductSearchEntity;
import com.ecommerce.core.domain.search.entity.SearchCriteria;
import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {
    
    private final ElasticsearchOperations elasticsearchOperations;
    
    @Override
    public Page<SearchResult> search(SearchCriteria criteria) {
        CriteriaQuery query = buildQuery(criteria);
        
        SearchHits<ProductSearchEntity> searchHits = elasticsearchOperations.search(query, ProductSearchEntity.class);
        
        List<SearchResult> results = searchHits.getSearchHits().stream()
                .map(this::mapToSearchResult)
                .collect(Collectors.toList());
        
        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getSize());
        return new PageImpl<>(results, pageRequest, searchHits.getTotalHits());
    }
    
    @Override
    public void indexProduct(Long productId) {
        // Implementation to index a single product
        // This would typically fetch product data and index it
    }
    
    @Override
    public void deleteProduct(Long productId) {
        elasticsearchOperations.delete(productId.toString(), ProductSearchEntity.class);
    }
    
    @Override
    public void reindexAll() {
        // Implementation to reindex all products
        // This would typically be done in batches
    }
    
    private CriteriaQuery buildQuery(SearchCriteria criteria) {
        Criteria searchCriteria = new Criteria();
        
        if (criteria.getQuery() != null && !criteria.getQuery().isEmpty()) {
            searchCriteria = searchCriteria.and(
                    new Criteria("name").matches(criteria.getQuery())
                            .or(new Criteria("description").matches(criteria.getQuery()))
                            .or(new Criteria("tags").matches(criteria.getQuery()))
            );
        }
        
        if (criteria.getCategories() != null && !criteria.getCategories().isEmpty()) {
            searchCriteria = searchCriteria.and(new Criteria("category").in(criteria.getCategories()));
        }
        
        if (criteria.getMinPrice() != null) {
            searchCriteria = searchCriteria.and(new Criteria("price").greaterThanEqual(criteria.getMinPrice()));
        }
        
        if (criteria.getMaxPrice() != null) {
            searchCriteria = searchCriteria.and(new Criteria("price").lessThanEqual(criteria.getMaxPrice()));
        }
        
        if (criteria.isInStockOnly()) {
            searchCriteria = searchCriteria.and(new Criteria("inStock").is(true));
        }
        
        CriteriaQuery query = new CriteriaQuery(searchCriteria);
        
        // Add sorting
        if (criteria.getSortBy() != null) {
            Sort.Direction direction = "desc".equalsIgnoreCase(criteria.getSortDirection()) 
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            query.addSort(Sort.by(direction, criteria.getSortBy()));
        }
        
        // Add pagination
        query.setPageable(PageRequest.of(criteria.getPage(), criteria.getSize()));
        
        return query;
    }
    
    private SearchResult mapToSearchResult(SearchHit<ProductSearchEntity> hit) {
        ProductSearchEntity entity = hit.getContent();
        return SearchResult.builder()
                .id(Long.parseLong(entity.getId()))
                .name(entity.getName())
                .description(entity.getDescription())
                .sku(entity.getSku())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .category(entity.getCategory())
                .tags(entity.getTags())
                .inStock(entity.isInStock())
                .score(Double.valueOf(hit.getScore()))
                .build();
    }
}