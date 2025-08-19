package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.entity.OrderStatus;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private CreateOrderRequest request;
    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(BigDecimal.valueOf(50.00));

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(cartItem);

        request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setShippingAddress("123 Main St, Test City");
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() {
        // Given
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setUser(user);
        savedOrder.setStatus(OrderStatus.PENDING);
        
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        CreateOrderResponse response = createOrderUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getOrderId());
        verify(cartRepository).findByUserId(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should fail when cart is empty")
    void shouldFailWhenCartIsEmpty() {
        // Given
        cart.getItems().clear();
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        // When
        CreateOrderResponse response = createOrderUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Cart is empty", response.getErrorMessage());
        verify(cartRepository).findByUserId(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should fail when cart not found")
    void shouldFailWhenCartNotFound() {
        // Given
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // When
        CreateOrderResponse response = createOrderUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Cart not found", response.getErrorMessage());
        verify(cartRepository).findByUserId(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should calculate correct order total")
    void shouldCalculateCorrectOrderTotal() {
        // Given
        CartItem item2 = new CartItem();
        Product product2 = new Product();
        product2.setName("Product 2");
        item2.setProduct(product2);
        item2.setQuantity(1);
        item2.setPrice(BigDecimal.valueOf(30.00));
        cart.getItems().add(item2);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        // When
        CreateOrderResponse response = createOrderUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(orderRepository).save(argThat(order -> 
            order.getItems().size() == 2
        ));
    }
}
