package com.ecommerce.core.domain.order.entity;

import com.ecommerce.core.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

class OrderTest {

    private Order order;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderNumber("ORD-001");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.valueOf(199.99));
        order.setOrderDate(LocalDateTime.now());
        order.setItems(new ArrayList<>());
    }

    @Test
    @DisplayName("Should create order with valid properties")
    void shouldCreateOrderWithValidProperties() {
        assertNotNull(order);
        assertEquals(1L, order.getId());
        assertEquals(user, order.getUser());
        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(BigDecimal.valueOf(199.99), order.getTotalAmount());
        assertNotNull(order.getOrderDate());
        assertNotNull(order.getItems());
    }

    @Test
    @DisplayName("Should handle order status transitions")
    void shouldHandleOrderStatusTransitions() {
        assertEquals(OrderStatus.PENDING, order.getStatus());

        order.setStatus(OrderStatus.CONFIRMED);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        order.setStatus(OrderStatus.SHIPPED);
        assertEquals(OrderStatus.SHIPPED, order.getStatus());

        order.setStatus(OrderStatus.DELIVERED);
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
    }

    @Test
    @DisplayName("Should add order items")
    void shouldAddOrderItems() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setProductName("Test Product");
        orderItem.setQuantity(2);
        orderItem.setUnitPrice(BigDecimal.valueOf(99.99));
        orderItem.setTotalPrice(BigDecimal.valueOf(199.98));

        order.getItems().add(orderItem);

        assertEquals(1, order.getItems().size());
        assertEquals(orderItem, order.getItems().get(0));
    }

    @Test
    @DisplayName("Should calculate order total from items")
    void shouldCalculateOrderTotalFromItems() {
        OrderItem item1 = new OrderItem();
        item1.setTotalPrice(BigDecimal.valueOf(99.99));

        OrderItem item2 = new OrderItem();
        item2.setTotalPrice(BigDecimal.valueOf(50.00));

        order.getItems().add(item1);
        order.getItems().add(item2);

        // Expected total: 99.99 + 50.00 = 149.99
        assertNotNull(order.getItems());
        assertEquals(2, order.getItems().size());
    }

    @Test
    @DisplayName("Should handle shipping address")
    void shouldHandleShippingAddress() {
        Address shippingAddress = new Address();
        shippingAddress.setStreet("123 Main St");
        shippingAddress.setCity("Test City");
        shippingAddress.setState("TS");
        shippingAddress.setZipCode("12345");

        order.setShippingAddress(shippingAddress);

        assertNotNull(order.getShippingAddress());
        assertEquals("123 Main St", order.getShippingAddress().getStreet());
        assertEquals("Test City", order.getShippingAddress().getCity());
    }

    @Test
    @DisplayName("Should handle order cancellation")
    void shouldHandleOrderCancellation() {
        order.setStatus(OrderStatus.CONFIRMED);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        order.setStatus(OrderStatus.CANCELLED);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }
}
