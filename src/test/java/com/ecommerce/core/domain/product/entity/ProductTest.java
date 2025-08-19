package com.ecommerce.core.domain.product.entity;

import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.domain.product.valueobject.SKU;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("A test product description");
        product.setSku(new SKU("TEST-SKU-001"));
        product.setPrice(new Price(BigDecimal.valueOf(99.99)));
    }

    @Test
    @DisplayName("Should create product with valid properties")
    void shouldCreateProductWithValidProperties() {
        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("A test product description", product.getDescription());
        assertNotNull(product.getSku());
        assertNotNull(product.getPrice());
    }

    @Test
    @DisplayName("Should handle price updates")
    void shouldHandlePriceUpdates() {
        Price newPrice = new Price(BigDecimal.valueOf(149.99));
        product.setPrice(newPrice);
        assertEquals(newPrice, product.getPrice());
    }

    @Test
    @DisplayName("Should validate SKU uniqueness concept")
    void shouldValidateSkuUniqueness() {
        SKU sku1 = new SKU("UNIQUE-001");
        SKU sku2 = new SKU("UNIQUE-001");
        
        assertEquals(sku1.getValue(), sku2.getValue());
    }

    @Test
    @DisplayName("Should handle product category assignment")
    void shouldHandleProductCategoryAssignment() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        
        product.setCategory(category);
        assertEquals(category, product.getCategory());
        assertEquals("Electronics", product.getCategory().getName());
    }

    @Test
    @DisplayName("Should handle product availability")
    void shouldHandleProductAvailability() {
        product.setAvailable(true);
        assertTrue(product.isAvailable());
        
        product.setAvailable(false);
        assertFalse(product.isAvailable());
    }
}
