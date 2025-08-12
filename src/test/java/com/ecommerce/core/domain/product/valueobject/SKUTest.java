package com.ecommerce.core.domain.product.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class SKUTest {

    @Test
    @DisplayName("Should create valid SKU")
    void shouldCreateValidSku() {
        // Given & When
        SKU sku = new SKU("PROD-12345");

        // Then
        assertNotNull(sku);
        assertEquals("PROD-12345", sku.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null SKU")
    void shouldThrowExceptionForNullSku() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new SKU(null);
        });
    }

    @Test
    @DisplayName("Should throw exception for empty SKU")
    void shouldThrowExceptionForEmptySku() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new SKU("");
        });
    }

    @Test
    @DisplayName("Should throw exception for blank SKU")
    void shouldThrowExceptionForBlankSku() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new SKU("   ");
        });
    }

    @Test
    @DisplayName("Should validate SKU format")
    void shouldValidateSkuFormat() {
        // Given & When
        SKU validSku1 = new SKU("ABC-123");
        SKU validSku2 = new SKU("PROD_456");
        SKU validSku3 = new SKU("12345");

        // Then
        assertNotNull(validSku1);
        assertNotNull(validSku2);
        assertNotNull(validSku3);
    }

    @Test
    @DisplayName("Should handle SKU equality")
    void shouldHandleSkuEquality() {
        // Given
        SKU sku1 = new SKU("SAME-SKU");
        SKU sku2 = new SKU("SAME-SKU");
        SKU sku3 = new SKU("DIFFERENT-SKU");

        // Then
        assertEquals(sku1, sku2);
        assertNotEquals(sku1, sku3);
        assertEquals(sku1.hashCode(), sku2.hashCode());
    }

    @Test
    @DisplayName("Should convert SKU to string")
    void shouldConvertSkuToString() {
        // Given
        SKU sku = new SKU("TEST-SKU");

        // When
        String skuString = sku.toString();

        // Then
        assertTrue(skuString.contains("TEST-SKU"));
    }
}
