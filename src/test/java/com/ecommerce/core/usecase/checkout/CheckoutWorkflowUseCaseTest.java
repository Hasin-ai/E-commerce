package com.ecommerce.core.usecase.checkout;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.product.entity.Product;
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
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CheckoutWorkflowUseCaseTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private CheckoutWorkflowUseCase checkoutWorkflowUseCase;

    private CheckoutWorkflowRequest request;
    private User user;
    private Cart cart;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(99.99));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPrice(BigDecimal.valueOf(99.99));

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(Arrays.asList(cartItem));

        order = new Order();
        order.setId(1L);
        order.setUser(user);

        request = new CheckoutWorkflowRequest();
        request.setUserId(1L);
        request.setShippingAddress("123 Main St");
        request.setPaymentMethod("CREDIT_CARD");
        request.setCardToken("card_token_123");
    }

    @Test
    @DisplayName("Should complete checkout workflow successfully")
    void shouldCompleteCheckoutWorkflowSuccessfully() {
        // Given
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        // When
        CheckoutWorkflowResponse response = checkoutWorkflowUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getOrderId());
        verify(cartRepository).findByUserId(1L);
        verify(orderRepository).save(any(Order.class));
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should fail when cart is empty")
    void shouldFailWhenCartIsEmpty() {
        // Given
        cart.getItems().clear();
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        // When
        CheckoutWorkflowResponse response = checkoutWorkflowUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Cart is empty", response.getErrorMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should handle payment processing failure")
    void shouldHandlePaymentProcessingFailure() {
        // Given
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentRepository.save(any(Payment.class)))
            .thenThrow(new RuntimeException("Payment failed"));

        // When
        CheckoutWorkflowResponse response = checkoutWorkflowUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getErrorMessage().contains("Payment failed"));
    }

    @Test
    @DisplayName("Should validate shipping address")
    void shouldValidateShippingAddress() {
        // Given
        request.setShippingAddress(null);

        // When
        CheckoutWorkflowResponse response = checkoutWorkflowUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Shipping address is required", response.getErrorMessage());
    }
}
