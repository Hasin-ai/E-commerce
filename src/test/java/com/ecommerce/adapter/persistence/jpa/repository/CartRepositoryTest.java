package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import com.ecommerce.core.domain.cart.valueobject.CartStatus;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CartRepository cartRepository;

    @Test
    @DisplayName("Should save and find cart by user ID")
    void shouldSaveAndFindCartByUserId() {
        // Given
        User user = new User();
        user.setUsername("cartuser");
        user.setEmail("cartuser@example.com");
        entityManager.persistAndFlush(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);

        // When
        Cart savedCart = cartRepository.save(cart);
        Optional<Cart> foundCart = cartRepository.findByUserId(user.getId());

        // Then
        assertTrue(foundCart.isPresent());
        assertEquals(user.getId(), foundCart.get().getUser().getId());
        assertEquals(CartStatus.ACTIVE, foundCart.get().getStatus());
    }

    @Test
    @DisplayName("Should find carts by status")
    void shouldFindCartsByStatus() {
        // Given
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        entityManager.persistAndFlush(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        entityManager.persistAndFlush(user2);

        Cart activeCart = new Cart();
        activeCart.setUser(user1);
        activeCart.setStatus(CartStatus.ACTIVE);

        Cart abandonedCart = new Cart();
        abandonedCart.setUser(user2);
        abandonedCart.setStatus(CartStatus.ABANDONED);

        entityManager.persistAndFlush(activeCart);
        entityManager.persistAndFlush(abandonedCart);

        // When
        List<Cart> activeCarts = cartRepository.findByStatus(CartStatus.ACTIVE);

        // Then
        assertEquals(1, activeCarts.size());
        assertEquals(CartStatus.ACTIVE, activeCarts.get(0).getStatus());
    }

    @Test
    @DisplayName("Should delete cart by user ID")
    void shouldDeleteCartByUserId() {
        // Given
        User user = new User();
        user.setUsername("deleteuser");
        user.setEmail("deleteuser@example.com");
        entityManager.persistAndFlush(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);
        entityManager.persistAndFlush(cart);

        // When
        cartRepository.deleteByUserId(user.getId());
        Optional<Cart> foundCart = cartRepository.findByUserId(user.getId());

        // Then
        assertFalse(foundCart.isPresent());
    }

    @Test
    @DisplayName("Should save cart with items")
    void shouldSaveCartWithItems() {
        // Given
        User user = new User();
        user.setUsername("itemuser");
        user.setEmail("itemuser@example.com");
        entityManager.persistAndFlush(user);

        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(50.00));
        entityManager.persistAndFlush(product);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(product.getPrice());

        cart.getItems().add(cartItem);

        // When
        Cart savedCart = cartRepository.save(cart);

        // Then
        assertNotNull(savedCart.getId());
        assertEquals(1, savedCart.getItems().size());
        assertEquals(product, savedCart.getItems().get(0).getProduct());
    }
}
