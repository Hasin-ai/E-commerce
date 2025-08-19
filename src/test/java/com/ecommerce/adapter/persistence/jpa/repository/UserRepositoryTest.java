package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.valueobject.UserRole;
import com.ecommerce.core.domain.user.valueobject.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import java.util.List;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and find user by ID")
    void shouldSaveAndFindUserById() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedPassword");
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        // When
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
        // Given
        User user = new User();
        user.setUsername("uniqueuser");
        user.setEmail("unique@example.com");
        user.setPasswordHash("hashedPassword");
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        entityManager.persistAndFlush(user);

        // When
        Optional<User> foundUser = userRepository.findByUsername("uniqueuser");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("uniqueuser", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // Given
        User user = new User();
        user.setUsername("emailuser");
        user.setEmail("email@example.com");
        user.setPasswordHash("hashedPassword");
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        entityManager.persistAndFlush(user);

        // When
        Optional<User> foundUser = userRepository.findByEmail("email@example.com");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("email@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Should find users by role")
    void shouldFindUsersByRole() {
        // Given
        User customer = new User();
        customer.setUsername("customer1");
        customer.setEmail("customer1@example.com");
        customer.setRole(UserRole.CUSTOMER);
        customer.setStatus(UserStatus.ACTIVE);

        User admin = new User();
        admin.setUsername("admin1");
        admin.setEmail("admin1@example.com");
        admin.setRole(UserRole.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);

        entityManager.persistAndFlush(customer);
        entityManager.persistAndFlush(admin);

        // When
        List<User> customers = userRepository.findByRole(UserRole.CUSTOMER);
        List<User> admins = userRepository.findByRole(UserRole.ADMIN);

        // Then
        assertFalse(customers.isEmpty());
        assertFalse(admins.isEmpty());
        assertTrue(customers.stream().allMatch(u -> u.getRole() == UserRole.CUSTOMER));
        assertTrue(admins.stream().allMatch(u -> u.getRole() == UserRole.ADMIN));
    }

    @Test
    @DisplayName("Should find users by status")
    void shouldFindUsersByStatus() {
        // Given
        User activeUser = new User();
        activeUser.setUsername("active1");
        activeUser.setEmail("active1@example.com");
        activeUser.setRole(UserRole.CUSTOMER);
        activeUser.setStatus(UserStatus.ACTIVE);

        User inactiveUser = new User();
        inactiveUser.setUsername("inactive1");
        inactiveUser.setEmail("inactive1@example.com");
        inactiveUser.setRole(UserRole.CUSTOMER);
        inactiveUser.setStatus(UserStatus.INACTIVE);

        entityManager.persistAndFlush(activeUser);
        entityManager.persistAndFlush(inactiveUser);

        // When
        List<User> activeUsers = userRepository.findByStatus(UserStatus.ACTIVE);

        // Then
        assertFalse(activeUsers.isEmpty());
        assertTrue(activeUsers.stream().allMatch(u -> u.getStatus() == UserStatus.ACTIVE));
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void shouldReturnEmptyWhenUserNotFound() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonexistent");

        // Then
        assertFalse(foundUser.isPresent());
    }
}
