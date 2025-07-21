package com.ecommerce.adapter.persistence.elasticsearch.repository;

import com.ecommerce.core.domain.product.entity.Product;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public interface ProductSearchRepository extends ElasticsearchRepository<Product, Long> {

    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^3\", \"description\"], \"fuzziness\": \"AUTO\"}}")
    Page<Product> search(String query, Pageable pageable);
}