package com.ecommerce.core.usecase.inventory;

import com.ecommerce.core.domain.inventory.entity.Inventory;
import com.ecommerce.core.domain.inventory.repository.InventoryRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
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

@ExtendWith(MockitoExtension.class)
class UpdateInventoryUseCaseTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private UpdateInventoryUseCase updateInventoryUseCase;

    private UpdateInventoryRequest request;
    private Product product;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProduct(product);
        inventory.setQuantity(100);
        inventory.setReservedQuantity(0);

        request = new UpdateInventoryRequest();
        request.setProductId(1L);
        request.setQuantity(50);
        request.setOperation("ADD");
    }

    @Test
    @DisplayName("Should add inventory successfully")
    void shouldAddInventorySuccessfully() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct(product)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        UpdateInventoryResponse response = updateInventoryUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(150, response.getNewQuantity()); // 100 + 50
        verify(inventoryRepository).save(argThat(inv -> inv.getQuantity() == 150));
    }

    @Test
    @DisplayName("Should subtract inventory successfully")
    void shouldSubtractInventorySuccessfully() {
        // Given
        request.setOperation("SUBTRACT");
        request.setQuantity(30);
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct(product)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        UpdateInventoryResponse response = updateInventoryUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(70, response.getNewQuantity()); // 100 - 30
        verify(inventoryRepository).save(argThat(inv -> inv.getQuantity() == 70));
    }

    @Test
    @DisplayName("Should fail when product not found")
    void shouldFailWhenProductNotFound() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        UpdateInventoryResponse response = updateInventoryUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Product not found", response.getErrorMessage());
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    @DisplayName("Should fail when insufficient inventory for subtraction")
    void shouldFailWhenInsufficientInventoryForSubtraction() {
        // Given
        request.setOperation("SUBTRACT");
        request.setQuantity(150); // More than available
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct(product)).thenReturn(Optional.of(inventory));

        // When
        UpdateInventoryResponse response = updateInventoryUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Insufficient inventory", response.getErrorMessage());
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    @DisplayName("Should create inventory if not exists")
    void shouldCreateInventoryIfNotExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(inventoryRepository.findByProduct(product)).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        // When
        UpdateInventoryResponse response = updateInventoryUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(inventoryRepository).save(argThat(inv -> 
            inv.getProduct().equals(product) && inv.getQuantity() == 50));
    }

    @Test
    @DisplayName("Should validate operation type")
    void shouldValidateOperationType() {
        // Given
        request.setOperation("INVALID");

        // When
        UpdateInventoryResponse response = updateInventoryUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Invalid operation type", response.getErrorMessage());
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }
}
