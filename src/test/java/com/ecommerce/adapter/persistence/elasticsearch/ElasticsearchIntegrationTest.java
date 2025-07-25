package com.ecommerce.adapter.persistence.elasticsearch;

import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import com.ecommerce.adapter.persistence.elasticsearch.repository.ProductSearchRepository;
import com.ecommerce.core.usecase.product.ProductSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Elasticsearch functionality
 * Note: Requires Elasticsearch to be running on localhost:9200
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.elasticsearch.uris=http://localhost:9200",
    "spring.data.elasticsearch.repositories.enabled=true"
})
class ElasticsearchIntegrationTest {

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Test
    void testProductIndexingAndSearch() {
        // Given
        ProductDocument testProduct = ProductDocument.builder()
                .id("test-1")
                .name("Test iPhone")
                .description("A test smartphone for integration testing")
                .sku("TEST-IPHONE-001")
                .price(new BigDecimal("799.99"))
                .stockQuantity(10)
                .categoryId("electronics")
                .categoryName("Electronics")
                .brand("Apple")
                .tags(Arrays.asList("smartphone", "test", "apple"))
                .active(true)
                .rating(4.5)
                .reviewCount(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When - Index the product
        ProductDocument savedProduct = productSearchService.indexProduct(testProduct);

        // Then - Verify the product was indexed
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo("test-1");

        // Wait a moment for Elasticsearch to index the document
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Test search functionality
        Page<ProductDocument> searchResults = productSearchService.searchProducts("iPhone", PageRequest.of(0, 10));
        assertThat(searchResults.getContent()).isNotEmpty();

        // Clean up
        productSearchService.deleteProduct("test-1");
    }

    @Test
    void testSearchByCategory() {
        // Given
        ProductDocument testProduct = ProductDocument.builder()
                .id("test-2")
                .name("Test Laptop")
                .description("A test laptop for category search testing")
                .sku("TEST-LAPTOP-001")
                .price(new BigDecimal("1299.99"))
                .stockQuantity(5)
                .categoryId("electronics")
                .categoryName("Electronics")
                .brand("Dell")
                .tags(Arrays.asList("laptop", "computer", "test"))
                .active(true)
                .rating(4.2)
                .reviewCount(50)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        productSearchService.indexProduct(testProduct);

        // Wait for indexing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Then
        Page<ProductDocument> categoryResults = productSearchService.searchByCategory("Electronics", PageRequest.of(0, 10));
        assertThat(categoryResults.getContent()).isNotEmpty();

        // Clean up
        productSearchService.deleteProduct("test-2");
    }

    @Test
    void testSearchByPriceRange() {
        // Given
        ProductDocument cheapProduct = ProductDocument.builder()
                .id("test-3")
                .name("Cheap Product")
                .description("An affordable test product")
                .sku("TEST-CHEAP-001")
                .price(new BigDecimal("50.00"))
                .stockQuantity(100)
                .categoryId("misc")
                .categoryName("Miscellaneous")
                .brand("TestBrand")
                .tags(Arrays.asList("cheap", "test"))
                .active(true)
                .rating(3.5)
                .reviewCount(25)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductDocument expensiveProduct = ProductDocument.builder()
                .id("test-4")
                .name("Expensive Product")
                .description("A premium test product")
                .sku("TEST-EXPENSIVE-001")
                .price(new BigDecimal("2000.00"))
                .stockQuantity(2)
                .categoryId("luxury")
                .categoryName("Luxury")
                .brand("PremiumBrand")
                .tags(Arrays.asList("expensive", "premium", "test"))
                .active(true)
                .rating(4.8)
                .reviewCount(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        productSearchService.indexProduct(cheapProduct);
        productSearchService.indexProduct(expensiveProduct);

        // Wait for indexing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Then
        Page<ProductDocument> lowPriceResults = productSearchService.searchByPriceRange(
                new BigDecimal("0"), new BigDecimal("100"), PageRequest.of(0, 10));

        Page<ProductDocument> highPriceResults = productSearchService.searchByPriceRange(
                new BigDecimal("1500"), new BigDecimal("3000"), PageRequest.of(0, 10));

        assertThat(lowPriceResults.getContent()).isNotEmpty();
        assertThat(highPriceResults.getContent()).isNotEmpty();

        // Clean up
        productSearchService.deleteProduct("test-3");
        productSearchService.deleteProduct("test-4");
    }

    @Test
    void testSearchBySku() {
        // Given
        ProductDocument testProduct = ProductDocument.builder()
                .id("test-5")
                .name("SKU Test Product")
                .description("A product for SKU search testing")
                .sku("UNIQUE-SKU-12345")
                .price(new BigDecimal("199.99"))
                .stockQuantity(15)
                .categoryId("test")
                .categoryName("Test Category")
                .brand("TestBrand")
                .tags(Arrays.asList("sku", "test", "unique"))
                .active(true)
                .rating(4.0)
                .reviewCount(75)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        productSearchService.indexProduct(testProduct);

        // Wait for indexing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Then
        Optional<ProductDocument> skuResult = productSearchService.findBySku("UNIQUE-SKU-12345");
        assertThat(skuResult).isPresent();
        assertThat(skuResult.get().getName()).isEqualTo("SKU Test Product");

        // Clean up
        productSearchService.deleteProduct("test-5");
    }
}
