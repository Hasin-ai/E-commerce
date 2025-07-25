package com.ecommerce.adapter.persistence.elasticsearch.repository;

import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {

    // Full-text search across name and description
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^2\", \"description\", \"brand\"]}}")
    Page<ProductDocument> findByFullTextSearch(String searchTerm, Pageable pageable);

    // Search by category
    Page<ProductDocument> findByCategoryNameAndActiveTrue(String categoryName, Pageable pageable);

    // Search by vendor
    Page<ProductDocument> findByVendorNameAndActiveTrue(String vendorName, Pageable pageable);

    // Search by price range
    Page<ProductDocument> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Search by tags
    Page<ProductDocument> findByTagsInAndActiveTrue(List<String> tags, Pageable pageable);

    // Search by brand
    Page<ProductDocument> findByBrandAndActiveTrue(String brand, Pageable pageable);

    // Advanced search with multiple filters
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^2\", \"description\", \"brand\"]}}, {\"term\": {\"active\": true}}], \"filter\": [{\"range\": {\"price\": {\"gte\": ?1, \"lte\": ?2}}}]}}")
    Page<ProductDocument> findBySearchTermAndPriceRange(String searchTerm, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Search for products with high rating
    Page<ProductDocument> findByRatingGreaterThanEqualAndActiveTrue(Double minRating, Pageable pageable);

    // Search by SKU (exact match)
    ProductDocument findBySkuAndActiveTrue(String sku);

    // Get all active products
    Page<ProductDocument> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);
}
