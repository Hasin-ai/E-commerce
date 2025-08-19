package com.ecommerce.core.domain.user.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Test
    @DisplayName("Should create valid phone number")
    void shouldCreateValidPhoneNumber() {
        // Given & When
        Phone phone = new Phone("123-456-7890");

        // Then
        assertNotNull(phone);
        assertEquals("123-456-7890", phone.getValue());
    }

    @Test
    @DisplayName("Should throw exception for null phone")
    void shouldThrowExceptionForNullPhone() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Phone(null);
        });
    }

    @Test
    @DisplayName("Should throw exception for empty phone")
    void shouldThrowExceptionForEmptyPhone() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Phone("");
        });
    }

    @Test
    @DisplayName("Should validate phone format")
    void shouldValidatePhoneFormat() {
        // Given & When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new Phone("invalid-phone");
        });
    }

    @Test
    @DisplayName("Should handle different valid phone formats")
    void shouldHandleDifferentValidPhoneFormats() {
        // Given & When
        Phone phone1 = new Phone("(123) 456-7890");
        Phone phone2 = new Phone("123.456.7890");
        Phone phone3 = new Phone("1234567890");

        // Then
        assertNotNull(phone1);
        assertNotNull(phone2);
        assertNotNull(phone3);
    }

    @Test
    @DisplayName("Should format phone number consistently")
    void shouldFormatPhoneNumberConsistently() {
        // Given
        Phone phone = new Phone("1234567890");

        // When
        String formatted = phone.getFormatted();

        // Then
        assertEquals("(123) 456-7890", formatted);
    }

    @Test
    @DisplayName("Should handle phone equality")
    void shouldHandlePhoneEquality() {
        // Given
        Phone phone1 = new Phone("123-456-7890");
        Phone phone2 = new Phone("123-456-7890");
        Phone phone3 = new Phone("987-654-3210");

        // Then
        assertEquals(phone1, phone2);
        assertNotEquals(phone1, phone3);
        assertEquals(phone1.hashCode(), phone2.hashCode());
    }
}
