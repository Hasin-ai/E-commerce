package com.ecommerce.core.domain.product.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

class PriceTest {

    @Test
    @DisplayName("Should create valid price")
    void shouldCreateValidPrice() {
        // Given & When
        Price price = new Price(BigDecimal.valueOf(99.99));

        // Then
        assertNotNull(price);
        assertEquals(BigDecimal.valueOf(99.99), price.getAmount());
    }

    @Test
    @DisplayName("Should throw exception for negative price")
    void shouldThrowExceptionForNegativePrice() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Price(BigDecimal.valueOf(-10.00));
        });
    }

    @Test
    @DisplayName("Should throw exception for null price")
    void shouldThrowExceptionForNullPrice() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Price(null);
        });
    }

    @Test
    @DisplayName("Should allow zero price")
    void shouldAllowZeroPrice() {
        // Given & When
        Price price = new Price(BigDecimal.ZERO);

        // Then
        assertNotNull(price);
        assertEquals(BigDecimal.ZERO, price.getAmount());
    }

    @Test
    @DisplayName("Should compare prices correctly")
    void shouldComparePricesCorrectly() {
        // Given
        Price price1 = new Price(BigDecimal.valueOf(50.00));
        Price price2 = new Price(BigDecimal.valueOf(100.00));
        Price price3 = new Price(BigDecimal.valueOf(50.00));

        // Then
        assertTrue(price1.compareTo(price2) < 0);
        assertTrue(price2.compareTo(price1) > 0);
        assertEquals(0, price1.compareTo(price3));
    }

    @Test
    @DisplayName("Should calculate price with tax")
    void shouldCalculatePriceWithTax() {
        // Given
        Price basePrice = new Price(BigDecimal.valueOf(100.00));
        BigDecimal taxRate = BigDecimal.valueOf(0.10); // 10%

        // When
        Price priceWithTax = basePrice.addTax(taxRate);

        // Then
        assertEquals(BigDecimal.valueOf(110.00), priceWithTax.getAmount());
    }

    @Test
    @DisplayName("Should apply discount correctly")
    void shouldApplyDiscountCorrectly() {
        // Given
        Price originalPrice = new Price(BigDecimal.valueOf(100.00));
        BigDecimal discountPercentage = BigDecimal.valueOf(0.20); // 20%

        // When
        Price discountedPrice = originalPrice.applyDiscount(discountPercentage);

        // Then
        assertEquals(BigDecimal.valueOf(80.00), discountedPrice.getAmount());
    }
}
