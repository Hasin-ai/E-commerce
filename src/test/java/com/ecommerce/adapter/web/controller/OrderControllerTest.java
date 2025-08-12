package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.order.CreateOrderUseCase;
import com.ecommerce.core.usecase.order.GetOrderUseCase;
import com.ecommerce.core.usecase.order.CreateOrderRequest;
import com.ecommerce.core.usecase.order.CreateOrderResponse;
import com.ecommerce.core.usecase.order.GetOrderRequest;
import com.ecommerce.core.usecase.order.GetOrderResponse;
import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.entity.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @MockBean
    private GetOrderUseCase getOrderUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateOrderRequest createOrderRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setUserId(1L);
        createOrderRequest.setShippingAddress("123 Main St, Test City");

        order = new Order();
        order.setId(1L);
        order.setOrderNumber("ORD-001");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.valueOf(199.99));
        order.setOrderDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() throws Exception {
        // Given
        CreateOrderResponse response = new CreateOrderResponse();
        response.setSuccess(true);
        response.setOrderId(1L);
        response.setOrderNumber("ORD-001");
        response.setMessage("Order created successfully");

        when(createOrderUseCase.execute(any(CreateOrderRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.orderId").value(1L))
                .andExpect(jsonPath("$.orderNumber").value("ORD-001"));
    }

    @Test
    @DisplayName("Should get order by ID successfully")
    void shouldGetOrderByIdSuccessfully() throws Exception {
        // Given
        GetOrderResponse response = new GetOrderResponse();
        response.setSuccess(true);
        response.setOrder(order);

        when(getOrderUseCase.execute(any(GetOrderRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.order.id").value(1L))
                .andExpect(jsonPath("$.order.orderNumber").value("ORD-001"));
    }

    @Test
    @DisplayName("Should handle create order failure")
    void shouldHandleCreateOrderFailure() throws Exception {
        // Given
        CreateOrderResponse response = new CreateOrderResponse();
        response.setSuccess(false);
        response.setErrorMessage("Cart is empty");

        when(createOrderUseCase.execute(any(CreateOrderRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Cart is empty"));
    }

    @Test
    @DisplayName("Should get user orders")
    void shouldGetUserOrders() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/orders/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle order not found")
    void shouldHandleOrderNotFound() throws Exception {
        // Given
        GetOrderResponse response = new GetOrderResponse();
        response.setSuccess(false);
        response.setErrorMessage("Order not found");

        when(getOrderUseCase.execute(any(GetOrderRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/orders/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Order not found"));
    }
}
