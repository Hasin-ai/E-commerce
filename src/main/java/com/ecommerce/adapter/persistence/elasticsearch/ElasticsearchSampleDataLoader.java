package com.ecommerce.adapter.persistence.elasticsearch;

import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.usecase.product.ProductSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Sample data loader for testing Elasticsearch functionality
 * Only runs in dev profile when Elasticsearch is enabled
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchSampleDataLoader implements CommandLineRunner {

    private final ProductSearchService productSearchService;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Loading sample data into Elasticsearch...");
            loadSampleProducts();
            testSearchFunctionality();
        } catch (Exception e) {
            log.warn("Could not load sample data into Elasticsearch: {}", e.getMessage());
        }
    }

    private void loadSampleProducts() {
        // Sample product 1
        ProductDocument product1 = ProductDocument.builder()
                .id("1")
                .name("iPhone 15 Pro")
                .description("Latest Apple iPhone with advanced camera system and titanium design")
                .sku("IPHONE15PRO-128")
                .price(new BigDecimal("999.99"))
                .stockQuantity(50)
                .categoryId("1")
                .categoryName("Electronics")
                .vendorId("apple")
                .vendorName("Apple Inc.")
                .brand("Apple")
                .tags(Arrays.asList("smartphone", "mobile", "apple", "premium", "iphone"))
                .active(true)
                .rating(4.8)
                .reviewCount(1250)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Sample product 2
        ProductDocument product2 = ProductDocument.builder()
                .id("2")
                .name("Samsung Galaxy S24")
                .description("Powerful Android smartphone with excellent display and camera capabilities")
                .sku("GALAXY-S24-256")
                .price(new BigDecimal("849.99"))
                .stockQuantity(75)
                .categoryId("1")
                .categoryName("Electronics")
                .vendorId("samsung")
                .vendorName("Samsung Electronics")
                .brand("Samsung")
                .tags(Arrays.asList("smartphone", "android", "samsung", "flagship", "galaxy"))
                .active(true)
                .rating(4.6)
                .reviewCount(890)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Sample product 3
        ProductDocument product3 = ProductDocument.builder()
                .id("3")
                .name("Nike Air Max 270")
                .description("Comfortable running shoes with Air Max technology for superior cushioning")
                .sku("NIKE-AM270-BLK-42")
                .price(new BigDecimal("129.99"))
                .stockQuantity(120)
                .categoryId("2")
                .categoryName("Footwear")
                .vendorId("nike")
                .vendorName("Nike Inc.")
                .brand("Nike")
                .tags(Arrays.asList("shoes", "running", "sports", "comfortable", "nike", "airmax"))
                .active(true)
                .rating(4.4)
                .reviewCount(2340)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Sample product 4
        ProductDocument product4 = ProductDocument.builder()
                .id("4")
                .name("MacBook Pro 14-inch")
                .description("Professional laptop with M3 chip for developers and creatives")
                .sku("MBP14-M3-512")
                .price(new BigDecimal("1999.99"))
                .stockQuantity(25)
                .categoryId("1")
                .categoryName("Electronics")
                .vendorId("apple")
                .vendorName("Apple Inc.")
                .brand("Apple")
                .tags(Arrays.asList("laptop", "computer", "apple", "macbook", "professional"))
                .active(true)
                .rating(4.9)
                .reviewCount(567)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Sample product 5
        ProductDocument product5 = ProductDocument.builder()
                .id("5")
                .name("Adidas UltraBoost 22")
                .description("Premium running shoes with Boost technology and Primeknit upper")
                .sku("ADIDAS-UB22-WHT-43")
                .price(new BigDecimal("179.99"))
                .stockQuantity(80)
                .categoryId("2")
                .categoryName("Footwear")
                .vendorId("adidas")
                .vendorName("Adidas AG")
                .brand("Adidas")
                .tags(Arrays.asList("shoes", "running", "sports", "boost", "adidas", "premium"))
                .active(true)
                .rating(4.5)
                .reviewCount(1890)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productSearchService.indexProduct(product1);
        productSearchService.indexProduct(product2);
        productSearchService.indexProduct(product3);
        productSearchService.indexProduct(product4);
        productSearchService.indexProduct(product5);

        log.info("Successfully loaded {} sample products into Elasticsearch!", 5);
    }

    private void testSearchFunctionality() {
        // Test full-text search
        Page<ProductDocument> phoneResults = productSearchService.searchProducts("phone", PageRequest.of(0, 10));
        log.info("Found {} products for 'phone' search", phoneResults.getTotalElements());

        // Test category search
        Page<ProductDocument> electronicsResults = productSearchService.searchByCategory("Electronics", PageRequest.of(0, 10));
        log.info("Found {} products in Electronics category", electronicsResults.getTotalElements());

        // Test brand search
        Page<ProductDocument> appleResults = productSearchService.searchByBrand("Apple", PageRequest.of(0, 10));
        log.info("Found {} Apple products", appleResults.getTotalElements());

        // Test price range search
        Page<ProductDocument> priceRangeResults = productSearchService.searchByPriceRange(
                new BigDecimal("100"), new BigDecimal("500"), PageRequest.of(0, 10));
        log.info("Found {} products in $100-$500 price range", priceRangeResults.getTotalElements());

        // Test tags search
        Page<ProductDocument> sportsResults = productSearchService.searchByTags(
                Arrays.asList("sports", "running"), PageRequest.of(0, 10));
        log.info("Found {} sports products", sportsResults.getTotalElements());

        // Test high-rated products
        Page<ProductDocument> highRatedResults = productSearchService.getHighRatedProducts(4.5, PageRequest.of(0, 10));
        log.info("Found {} high-rated products (4.5+ stars)", highRatedResults.getTotalElements());

        log.info("Elasticsearch search functionality tested successfully!");
    }
}
