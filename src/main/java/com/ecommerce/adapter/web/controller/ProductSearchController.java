package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import com.ecommerce.core.usecase.product.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/search")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDocument>> searchProducts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductDocument> results = productSearchService.searchProducts(query, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/products/category/{categoryName}")
    public ResponseEntity<Page<ProductDocument>> searchByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> results = productSearchService.searchByCategory(categoryName, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/products/vendor/{vendorName}")
    public ResponseEntity<Page<ProductDocument>> searchByVendor(
            @PathVariable String vendorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> results = productSearchService.searchByVendor(vendorName, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/products/price-range")
    public ResponseEntity<Page<ProductDocument>> searchByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> results = productSearchService.searchByPriceRange(minPrice, maxPrice, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/products/tags")
    public ResponseEntity<Page<ProductDocument>> searchByTags(
            @RequestParam String tags, // comma-separated tags
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<String> tagList = Arrays.asList(tags.split(","));
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> results = productSearchService.searchByTags(tagList, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/products/brand/{brand}")
    public ResponseEntity<Page<ProductDocument>> searchByBrand(
            @PathVariable String brand,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> results = productSearchService.searchByBrand(brand, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/products/advanced")
    public ResponseEntity<Page<ProductDocument>> advancedSearch(
            @RequestParam String query,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        if (minPrice != null && maxPrice != null) {
            Page<ProductDocument> results = productSearchService.advancedSearch(query, minPrice, maxPrice, pageable);
            return ResponseEntity.ok(results);
        } else {
            Page<ProductDocument> results = productSearchService.searchProducts(query, pageable);
            return ResponseEntity.ok(results);
        }
    }

    @GetMapping("/products/top-rated")
    public ResponseEntity<Page<ProductDocument>> getTopRatedProducts(
            @RequestParam(defaultValue = "4.0") Double minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "rating"));
        Page<ProductDocument> results = productSearchService.getHighRatedProducts(minRating, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/products/sku/{sku}")
    public ResponseEntity<ProductDocument> findBySku(@PathVariable String sku) {
        Optional<ProductDocument> product = productSearchService.findBySku(sku);
        return product.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products/all")
    public ResponseEntity<Page<ProductDocument>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> results = productSearchService.getAllActiveProducts(pageable);
        return ResponseEntity.ok(results);
    }
}
