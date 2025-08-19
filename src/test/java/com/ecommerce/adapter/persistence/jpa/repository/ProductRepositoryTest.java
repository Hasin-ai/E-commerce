package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save and find product by ID")
    void shouldSaveAndFindProductById() {
        // Given
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("A test product");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setAvailable(true);

        // When
        Product savedProduct = productRepository.save(product);
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // Then
        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
        assertEquals(BigDecimal.valueOf(99.99), foundProduct.get().getPrice());
    }

    @Test
    @DisplayName("Should find products by category")
    void shouldFindProductsByCategory() {
        // Given
        Category category = new Category();
        category.setName("Electronics");
        entityManager.persistAndFlush(category);

        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setCategory(category);
        product1.setAvailable(true);

        Product product2 = new Product();
        product2.setName("Phone");
        product2.setCategory(category);
        product2.setAvailable(true);

        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);

        // When
        List<Product> products = productRepository.findByCategory(category);

        // Then
        assertEquals(2, products.size());
        assertTrue(products.stream().allMatch(p -> p.getCategory().equals(category)));
    }

    @Test
    @DisplayName("Should find products by availability")
    void shouldFindProductsByAvailability() {
        // Given
        Product availableProduct = new Product();
        availableProduct.setName("Available Product");
        availableProduct.setAvailable(true);

        Product unavailableProduct = new Product();
        unavailableProduct.setName("Unavailable Product");
        unavailableProduct.setAvailable(false);

        entityManager.persistAndFlush(availableProduct);
        entityManager.persistAndFlush(unavailableProduct);

        // When
        List<Product> availableProducts = productRepository.findByAvailableTrue();

        // Then
        assertFalse(availableProducts.isEmpty());
        assertTrue(availableProducts.stream().allMatch(Product::isAvailable));
    }

    @Test
    @DisplayName("Should find products by price range")
    void shouldFindProductsByPriceRange() {
        // Given
        Product cheapProduct = new Product();
        cheapProduct.setName("Cheap Product");
        cheapProduct.setPrice(BigDecimal.valueOf(10.00));

        Product expensiveProduct = new Product();
        expensiveProduct.setName("Expensive Product");
        expensiveProduct.setPrice(BigDecimal.valueOf(100.00));

        Product midRangeProduct = new Product();
        midRangeProduct.setName("Mid Range Product");
        midRangeProduct.setPrice(BigDecimal.valueOf(50.00));

        entityManager.persistAndFlush(cheapProduct);
        entityManager.persistAndFlush(expensiveProduct);
        entityManager.persistAndFlush(midRangeProduct);

        // When
        List<Product> productsInRange = productRepository.findByPriceBetween(
            BigDecimal.valueOf(20.00), 
            BigDecimal.valueOf(80.00)
        );

        // Then
        assertEquals(1, productsInRange.size());
        assertEquals("Mid Range Product", productsInRange.get(0).getName());
    }

    @Test
    @DisplayName("Should search products by name containing")
    void shouldSearchProductsByNameContaining() {
        // Given
        Product laptop = new Product();
        laptop.setName("Gaming Laptop");

        Product phone = new Product();
        phone.setName("Smart Phone");

        Product tablet = new Product();
        tablet.setName("Tablet Device");

        entityManager.persistAndFlush(laptop);
        entityManager.persistAndFlush(phone);
        entityManager.persistAndFlush(tablet);

        // When
        List<Product> searchResults = productRepository.findByNameContainingIgnoreCase("laptop");

        // Then
        assertEquals(1, searchResults.size());
        assertEquals("Gaming Laptop", searchResults.get(0).getName());
    }

    @Test
    @DisplayName("Should find products with pagination")
    void shouldFindProductsWithPagination() {
        // Given
        for (int i = 1; i <= 15; i++) {
            Product product = new Product();
            product.setName("Product " + i);
            product.setPrice(BigDecimal.valueOf(i * 10));
            entityManager.persistAndFlush(product);
        }

        // When
        Page<Product> firstPage = productRepository.findAll(PageRequest.of(0, 10));
        Page<Product> secondPage = productRepository.findAll(PageRequest.of(1, 10));

        // Then
        assertEquals(10, firstPage.getContent().size());
        assertEquals(5, secondPage.getContent().size());
        assertEquals(15, firstPage.getTotalElements());
    }
}
