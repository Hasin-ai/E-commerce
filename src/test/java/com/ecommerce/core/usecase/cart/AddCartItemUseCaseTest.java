package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.cart.valueobject.Quantity;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
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
import java.util.Optional;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class AddCartItemUseCaseTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private AddCartItemUseCase addCartItemUseCase;

    private AddCartItemRequest request;
    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setAvailable(true);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        request = new AddCartItemRequest();
        request.setUserId(1L);
        request.setProductId(1L);
        request.setQuantity(2);
    }

    @Test
    @DisplayName("Should add item to cart successfully")
    void shouldAddItemToCartSuccessfully() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        AddCartItemResponse response = addCartItemUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(productRepository).findById(1L);
        verify(cartRepository).findByUserId(1L);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when product not found")
    void shouldFailWhenProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        AddCartItemResponse response = addCartItemUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Product not found", response.getErrorMessage());
        verify(productRepository).findById(1L);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when product is not available")
    void shouldFailWhenProductNotAvailable() {
        // Given
        product.setAvailable(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        AddCartItemResponse response = addCartItemUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Product is not available", response.getErrorMessage());
        verify(productRepository).findById(1L);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should create new cart if user has no cart")
    void shouldCreateNewCartIfUserHasNoCart() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        AddCartItemResponse response = addCartItemUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(productRepository).findById(1L);
        verify(cartRepository).findByUserId(1L);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should update quantity if item already exists in cart")
    void shouldUpdateQuantityIfItemAlreadyExistsInCart() {
        // Given
        CartItem existingItem = new CartItem();
        existingItem.setProduct(product);
        existingItem.setQuantity(new Quantity(1));
        cart.getItems().add(existingItem);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // When
        AddCartItemResponse response = addCartItemUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(cartRepository).save(any(Cart.class));
    }
}
