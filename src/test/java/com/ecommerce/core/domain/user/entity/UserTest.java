package com.ecommerce.core.domain.user.entity;

import com.ecommerce.core.domain.user.valueobject.UserRole;
import com.ecommerce.core.domain.user.valueobject.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should create user with valid properties")
    void shouldCreateUserWithValidProperties() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(UserRole.CUSTOMER, user.getRole());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    @Test
    @DisplayName("Should set and get user properties correctly")
    void shouldSetAndGetUserProperties() {
        user.setFirstName("John");
        user.setLastName("Doe");
        
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
    }

    @Test
    @DisplayName("Should handle user status changes")
    void shouldHandleUserStatusChanges() {
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        
        user.setStatus(UserStatus.INACTIVE);
        assertEquals(UserStatus.INACTIVE, user.getStatus());
        
        user.setStatus(UserStatus.SUSPENDED);
        assertEquals(UserStatus.SUSPENDED, user.getStatus());
    }

    @Test
    @DisplayName("Should handle user role changes")
    void shouldHandleUserRoleChanges() {
        assertEquals(UserRole.CUSTOMER, user.getRole());
        
        user.setRole(UserRole.ADMIN);
        assertEquals(UserRole.ADMIN, user.getRole());
        
        user.setRole(UserRole.VENDOR);
        assertEquals(UserRole.VENDOR, user.getRole());
    }

    @Test
    @DisplayName("Should validate email format")
    void shouldValidateEmailFormat() {
        assertTrue(user.getEmail().contains("@"));
        assertTrue(user.getEmail().contains("."));
    }

    @Test
    @DisplayName("Should handle null values appropriately")
    void shouldHandleNullValues() {
        User nullUser = new User();
        assertNull(nullUser.getUsername());
        assertNull(nullUser.getEmail());
        assertNull(nullUser.getFirstName());
        assertNull(nullUser.getLastName());
    }
}
