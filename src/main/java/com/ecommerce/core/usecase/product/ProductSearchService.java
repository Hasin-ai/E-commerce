package com.ecommerce.core.usecase.product;

import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import com.ecommerce.adapter.persistence.elasticsearch.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;

    public Page<ProductDocument> searchProducts(String searchTerm, Pageable pageable) {
        log.info("Searching products with term: {}", searchTerm);
        return productSearchRepository.findByFullTextSearch(searchTerm, pageable);
    }

    public Page<ProductDocument> searchByCategory(String categoryName, Pageable pageable) {
        return productSearchRepository.findByCategoryNameAndActiveTrue(categoryName, pageable);
    }

    public Page<ProductDocument> searchByVendor(String vendorName, Pageable pageable) {
        return productSearchRepository.findByVendorNameAndActiveTrue(vendorName, pageable);
    }

    public Page<ProductDocument> searchByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productSearchRepository.findByPriceBetweenAndActiveTrue(minPrice, maxPrice, pageable);
    }

    public Page<ProductDocument> searchByTags(List<String> tags, Pageable pageable) {
        return productSearchRepository.findByTagsInAndActiveTrue(tags, pageable);
    }

    public Page<ProductDocument> searchByBrand(String brand, Pageable pageable) {
        return productSearchRepository.findByBrandAndActiveTrue(brand, pageable);
    }

    public Page<ProductDocument> advancedSearch(String searchTerm, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productSearchRepository.findBySearchTermAndPriceRange(searchTerm, minPrice, maxPrice, pageable);
    }

    public Page<ProductDocument> getHighRatedProducts(Double minRating, Pageable pageable) {
        return productSearchRepository.findByRatingGreaterThanEqualAndActiveTrue(minRating, pageable);
    }

    public Optional<ProductDocument> findBySku(String sku) {
        return Optional.ofNullable(productSearchRepository.findBySkuAndActiveTrue(sku));
    }

    public Page<ProductDocument> getAllActiveProducts(Pageable pageable) {
        return productSearchRepository.findByActiveTrueOrderByCreatedAtDesc(pageable);
    }

    public ProductDocument indexProduct(ProductDocument product) {
        log.info("Indexing product: {}", product.getName());
        return productSearchRepository.save(product);
    }

    public void deleteProduct(String productId) {
        log.info("Deleting product from index: {}", productId);
        productSearchRepository.deleteById(productId);
    }

    public boolean existsById(String productId) {
        return productSearchRepository.existsById(productId);
    }
}
