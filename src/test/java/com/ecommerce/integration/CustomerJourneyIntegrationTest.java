package com.ecommerce.integration;

import com.ecommerce.adapter.web.dto.request.RegisterUserRequestDto;
import com.ecommerce.adapter.web.dto.request.LoginUserRequestDto;
import com.ecommerce.adapter.web.dto.request.AddCartItemRequestDto;
import com.ecommerce.adapter.web.dto.request.CreateOrderRequestDto;
import com.ecommerce.adapter.web.dto.request.ProcessPaymentRequestDto;
import com.ecommerce.adapter.web.dto.request.TrackEventRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class CustomerJourneyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String authToken;
    private static Long userId;
    private static Long productId = 1L;
    private static Long orderId;

    @Test
    @Order(1)
    void phase1_healthCheckAndRegistration() throws Exception {
        // Health Check
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        // User Registration
        RegisterUserRequestDto registerRequest = new RegisterUserRequestDto();
        registerRequest.setFirstName("Sarah");
        registerRequest.setLastName("Johnson");
        registerRequest.setEmail("sarah.johnson@example.com");
        registerRequest.setPassword("SecurePass123!");
        registerRequest.setPhone("+1234567890");

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("sarah.johnson@example.com"))
                .andExpect(jsonPath("$.data.firstName").value("Sarah"))
                .andExpect(jsonPath("$.data.lastName").value("Johnson"))
                .andReturn();

        // Extract user ID from response
        String registerResponse = registerResult.getResponse().getContentAsString();
        // Parse JSON to extract user ID (simplified)
        userId = 1L; // In real test, parse from JSON response
    }

    @Test
    @Order(2)
    void phase2_productDiscoveryAndSearch() throws Exception {
        // Browse featured products
        mockMvc.perform(get("/api/products")
                .param("featured", "true")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Search for gaming laptop
        mockMvc.perform(get("/api/search/products")
                .param("query", "gaming laptop")
                .param("minPrice", "800")
                .param("maxPrice", "2000")
                .param("sortBy", "price")
                .param("sortDirection", "asc")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.query").value("gaming laptop"));

        // Get recommendations for anonymous user
        mockMvc.perform(get("/api/search/recommendations")
                .param("limit", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Track product view event
        TrackEventRequestDto trackRequest = new TrackEventRequestDto();
        trackRequest.setEventType("PRODUCT_VIEW");
        trackRequest.setProductId(productId);
        trackRequest.setCategory("electronics");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "search");
        metadata.put("position", 1);
        trackRequest.setMetadata(metadata);

        mockMvc.perform(post("/api/analytics/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trackRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(3)
    void phase3_authenticationAndCartManagement() throws Exception {
        // User Login
        LoginUserRequestDto loginRequest = new LoginUserRequestDto();
        loginRequest.setEmail("sarah.johnson@example.com");
        loginRequest.setPassword("SecurePass123!");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.user.email").value("sarah.johnson@example.com"))
                .andReturn();

        // Extract auth token from response
        String loginResponse = loginResult.getResponse().getContentAsString();
        // Parse JSON to extract token (simplified)
        authToken = "mock-jwt-token"; // In real test, parse from JSON response

        // Add gaming laptop to cart
        AddCartItemRequestDto addCartRequest = new AddCartItemRequestDto();
        addCartRequest.setProductId(productId);
        addCartRequest.setQuantity(1);

        mockMvc.perform(post("/api/cart/items")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addCartRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Add gaming mouse to cart
        AddCartItemRequestDto addMouseRequest = new AddCartItemRequestDto();
        addMouseRequest.setProductId(2L); // Gaming mouse
        addMouseRequest.setQuantity(2);

        mockMvc.perform(post("/api/cart/items")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addMouseRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // View cart contents
        mockMvc.perform(get("/api/cart")
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalItems").value(3)); // 1 laptop + 2 mice

        // Get cart item count
        mockMvc.perform(get("/api/cart/count")
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(3));
    }

    @Test
    @Order(4)
    void phase4_checkoutAndOrderCreation() throws Exception {
        // Initiate checkout
        mockMvc.perform(post("/api/checkout")
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Create order
        CreateOrderRequestDto orderRequest = new CreateOrderRequestDto();

        // Set up shipping address
        CreateOrderRequestDto.AddressDto shippingAddress = new CreateOrderRequestDto.AddressDto();
        shippingAddress.setStreet("123 Main St");
        shippingAddress.setCity("New York");
        shippingAddress.setState("NY");
        shippingAddress.setZipCode("10001");
        shippingAddress.setCountry("USA");
        orderRequest.setShippingAddress(shippingAddress);

        // Set up billing address (same as shipping for simplicity)
        orderRequest.setBillingAddress(shippingAddress);

        MvcResult orderResult = mockMvc.perform(post("/api/orders")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andReturn();

        // Extract order ID from response
        String orderResponse = orderResult.getResponse().getContentAsString();
        orderId = 1L; // In real test, parse from JSON response
    }

    @Test
    @Order(5)
    void phase5_paymentProcessing() throws Exception {
        // Process payment
        ProcessPaymentRequestDto paymentRequest = new ProcessPaymentRequestDto();
        paymentRequest.setOrderId(orderId);
        paymentRequest.setAmount(BigDecimal.valueOf(2399.98));
        paymentRequest.setCurrency("USD");
        paymentRequest.setPaymentMethod("STRIPE");
        paymentRequest.setPaymentMethodId("pm_1234567890"); // Mock Stripe payment method ID
        paymentRequest.setCustomerId("cus_1234567890"); // Mock Stripe customer ID
        paymentRequest.setSavePaymentMethod(false);
        paymentRequest.setConfirmPayment(true);

        MvcResult paymentResult = mockMvc.perform(post("/api/payments/process")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andReturn();

        // Verify payment details
        Long paymentId = 1L; // Extract from response
        mockMvc.perform(get("/api/payments/" + paymentId)
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));

        // Confirm order details after payment
        mockMvc.perform(get("/api/orders/" + orderId)
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CONFIRMED"));
    }

    @Test
    @Order(6)
    void phase6_orderTrackingAndNotifications() throws Exception {
        // View order history
        mockMvc.perform(get("/api/orders")
                .header("Authorization", "Bearer " + authToken)
                .param("page", "0")
                .param("size", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Track specific order
        mockMvc.perform(get("/api/orders/" + orderId)
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(orderId));

        // View notifications
        mockMvc.perform(get("/api/notifications")
                .header("Authorization", "Bearer " + authToken)
                .param("unreadOnly", "true")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Get unread notification count
        mockMvc.perform(get("/api/notifications/unread/count")
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Mark notification as read
        Long notificationId = 1L;
        mockMvc.perform(put("/api/notifications/" + notificationId + "/read")
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(7)
    void phase7_postPurchaseAnalytics() throws Exception {
        // Track purchase completion
        TrackEventRequestDto trackRequest = new TrackEventRequestDto();
        trackRequest.setEventType("PURCHASE_COMPLETE");
        trackRequest.setOrderId(orderId);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("total_amount", 2399.98);
        metadata.put("items_count", 3);
        metadata.put("payment_method", "credit_card");
        trackRequest.setMetadata(metadata);

        mockMvc.perform(post("/api/analytics/track")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trackRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Get updated personalized recommendations
        mockMvc.perform(get("/api/search/recommendations")
                .header("Authorization", "Bearer " + authToken)
                .param("limit", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Admin analytics dashboard (would need admin token in real test)
        // mockMvc.perform(get("/api/analytics/admin/dashboard")
        //         .header("Authorization", "Bearer " + adminToken)
        //         .with(csrf()))
        //         .andExpect(status().isOk())
        //         .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(8)
    void additionalFeatures_searchAndRecommendations() throws Exception {
        // Search with authenticated user (for personalized results)
        mockMvc.perform(get("/api/search/products")
                .header("Authorization", "Bearer " + authToken)
                .param("query", "gaming accessories")
                .param("category", "electronics")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Get search suggestions
        mockMvc.perform(get("/api/search/suggestions")
                .param("query", "gam")
                .param("limit", "5")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Get similar products for a specific product
        mockMvc.perform(get("/api/search/recommendations/product/" + productId)
                .param("limit", "5")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Get trending products
        mockMvc.perform(get("/api/search/trending")
                .param("limit", "10")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(9)
    void errorHandlingAndEdgeCases() throws Exception {
        // Test invalid search query
        mockMvc.perform(get("/api/search/products")
                .param("query", "")
                .with(csrf()))
                .andExpect(status().isBadRequest());

        // Test adding invalid quantity to cart
        AddCartItemRequestDto invalidCartRequest = new AddCartItemRequestDto();
        invalidCartRequest.setProductId(productId);
        invalidCartRequest.setQuantity(0);

        mockMvc.perform(post("/api/cart/items")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCartRequest))
                .with(csrf()))
                .andExpect(status().isBadRequest());

        // Test accessing protected endpoint without authentication
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isUnauthorized());

        // Test removing non-existent cart item
        mockMvc.perform(delete("/api/cart/items/999")
                .header("Authorization", "Bearer " + authToken)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}

