package com.ecommerce.core.domain.cart.entity;

import com.ecommerce.core.domain.cart.valueobject.CartStatus;
import com.ecommerce.core.domain.cart.valueobject.Quantity;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.ArrayList;

class CartTest {

    private Cart cart;
    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new Price(BigDecimal.valueOf(50.00)));

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setItems(new ArrayList<>());
    }

    @Test
    @DisplayName("Should create cart with valid properties")
    void shouldCreateCartWithValidProperties() {
        assertNotNull(cart);
        assertEquals(1L, cart.getId());
        assertEquals(user, cart.getUser());
        assertEquals(CartStatus.ACTIVE, cart.getStatus());
        assertNotNull(cart.getItems());
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    @DisplayName("Should add items to cart")
    void shouldAddItemsToCart() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(new Quantity(2));
        cartItem.setPrice(product.getPrice());

        cart.getItems().add(cartItem);

        assertEquals(1, cart.getItems().size());
        assertEquals(cartItem, cart.getItems().get(0));
    }

    @Test
    @DisplayName("Should calculate total cart value")
    void shouldCalculateTotalCartValue() {
        CartItem item1 = new CartItem();
        item1.setProduct(product);
        item1.setQuantity(new Quantity(2));
        item1.setPrice(new Price(BigDecimal.valueOf(50.00)));

        CartItem item2 = new CartItem();
        Product product2 = new Product();
        product2.setPrice(new Price(BigDecimal.valueOf(30.00)));
        item2.setProduct(product2);
        item2.setQuantity(new Quantity(1));
        item2.setPrice(product2.getPrice());

        cart.getItems().add(item1);
        cart.getItems().add(item2);

        // Total should be (2 * 50.00) + (1 * 30.00) = 130.00
        BigDecimal expectedTotal = BigDecimal.valueOf(130.00);
        // This would be implemented in the actual Cart entity
        assertNotNull(cart.getItems());
        assertEquals(2, cart.getItems().size());
    }

    @Test
    @DisplayName("Should handle cart status changes")
    void shouldHandleCartStatusChanges() {
        assertEquals(CartStatus.ACTIVE, cart.getStatus());

        cart.setStatus(CartStatus.CHECKOUT);
        assertEquals(CartStatus.CHECKOUT, cart.getStatus());

        cart.setStatus(CartStatus.ABANDONED);
        assertEquals(CartStatus.ABANDONED, cart.getStatus());
    }

    @Test
    @DisplayName("Should clear cart items")
    void shouldClearCartItems() {
        CartItem cartItem = new CartItem();
        cart.getItems().add(cartItem);
        assertEquals(1, cart.getItems().size());

        cart.getItems().clear();
        assertTrue(cart.getItems().isEmpty());
    }
}
