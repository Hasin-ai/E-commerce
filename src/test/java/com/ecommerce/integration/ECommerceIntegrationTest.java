package com.ecommerce.integration;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.util.Map;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ECommerceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUsername("integrationuser");
        testUser.setEmail("integration@example.com");
        testUser.setPasswordHash("hashedPassword");
        testUser = userRepository.save(testUser);

        // Create test product
        testProduct = new Product();
        testProduct.setName("Integration Test Product");
        testProduct.setPrice(BigDecimal.valueOf(99.99));
        testProduct.setAvailable(true);
        testProduct = productRepository.save(testProduct);
    }

    @Test
    @DisplayName("Should complete end-to-end user journey")
    void shouldCompleteEndToEndUserJourney() throws Exception {
        // 1. Register user (already done in setup)

        // 2. Add product to cart
        String addToCartRequest = objectMapper.writeValueAsString(Map.of(
            "userId", testUser.getId(),
            "productId", testProduct.getId(),
            "quantity", 2
        ));

        mockMvc.perform(post("/api/cart/add")
                .contentType("application/json")
                .content(addToCartRequest))
                .andExpect(status().isOk());

        // 3. Get cart
        mockMvc.perform(get("/api/cart/" + testUser.getId())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 4. Create order
        String createOrderRequest = objectMapper.writeValueAsString(Map.of(
            "userId", testUser.getId(),
            "shippingAddress", "123 Test Street"
        ));

        mockMvc.perform(post("/api/orders")
                .contentType("application/json")
                .content(createOrderRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should handle product search and recommendations")
    void shouldHandleProductSearchAndRecommendations() throws Exception {
        // Search products
        mockMvc.perform(get("/api/search/products")
                .param("query", "test")
                .param("page", "0")
                .param("size", "10")
                .contentType("application/json"))
                .andExpect(status().isOk());

        // Get recommendations
        mockMvc.perform(get("/api/search/recommendations/" + testProduct.getId())
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle inventory updates")
    void shouldHandleInventoryUpdates() throws Exception {
        String updateInventoryRequest = objectMapper.writeValueAsString(Map.of(
            "productId", testProduct.getId(),
            "quantity", 50,
            "operation", "ADD"
        ));

        mockMvc.perform(put("/api/inventory/update")
                .contentType("application/json")
                .content(updateInventoryRequest))
                .andExpect(status().isOk());
    }
}
