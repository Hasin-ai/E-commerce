package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.TrackEventRequestDto;
import com.ecommerce.adapter.web.dto.response.AnalyticsDashboardDto;
import com.ecommerce.config.TestSecurityConfig;
import com.ecommerce.core.usecase.analytics.TrackEventUseCase;
import com.ecommerce.core.usecase.analytics.TrackEventResponse;
import com.ecommerce.core.usecase.analytics.GetAnalyticsDashboardUseCase;
import com.ecommerce.core.usecase.analytics.GetAnalyticsDashboardResponse;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
@Import(TestSecurityConfig.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrackEventUseCase trackEventUseCase;

    @MockBean
    private GetAnalyticsDashboardUseCase getAnalyticsDashboardUseCase;

    @MockBean
    private UserRepository userRepository;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("test@example.com", "password123");
        mockUser.setId(1L);
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void trackEvent_WithValidRequest_ShouldTrackSuccessfully() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        TrackEventRequestDto requestDto = new TrackEventRequestDto();
        requestDto.setEventType("PRODUCT_VIEW");
        requestDto.setProductId(1L);
        requestDto.setCategory("Electronics");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("source", "search");
        metadata.put("position", 1);
        requestDto.setMetadata(metadata);

        TrackEventResponse response = TrackEventResponse.success(1L, "PRODUCT_VIEW");
        when(trackEventUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/analytics/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Event tracked successfully"));
    }

    @Test
    void trackEvent_WithAnonymousUser_ShouldTrackSuccessfully() throws Exception {
        // Given
        TrackEventRequestDto requestDto = new TrackEventRequestDto();
        requestDto.setEventType("PAGE_VIEW");
        requestDto.setPage("/products");

        TrackEventResponse response = TrackEventResponse.success(2L, "PAGE_VIEW");
        when(trackEventUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/analytics/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void trackEvent_WithInvalidEventType_ShouldReturnBadRequest() throws Exception {
        // Given
        TrackEventRequestDto requestDto = new TrackEventRequestDto();
        requestDto.setEventType(""); // Empty event type

        // When & Then
        mockMvc.perform(post("/api/analytics/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void trackEvent_WithTrackingFailure_ShouldReturnError() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        TrackEventRequestDto requestDto = new TrackEventRequestDto();
        requestDto.setEventType("PRODUCT_VIEW");
        requestDto.setProductId(1L);

        TrackEventResponse response = TrackEventResponse.failure("PRODUCT_VIEW", "Database error");
        when(trackEventUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/analytics/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Database error"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void getDashboardAnalytics_WithAdminUser_ShouldReturnAnalytics() throws Exception {
        // Given
        AnalyticsDashboardDto dashboard = AnalyticsDashboardDto.builder()
                .totalRevenue(BigDecimal.valueOf(125000.00))
                .totalOrders(450L)
                .totalCustomers(1200L)
                .conversionRate(BigDecimal.valueOf(3.2))
                .averageOrderValue(BigDecimal.valueOf(277.78))
                .build();

        GetAnalyticsDashboardResponse response = GetAnalyticsDashboardResponse.builder()
                .dashboard(dashboard)
                .build();

        when(getAnalyticsDashboardUseCase.execute(any(LocalDate.class), any(LocalDate.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/analytics/admin/dashboard")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalRevenue").value(125000.00))
                .andExpect(jsonPath("$.data.totalOrders").value(450))
                .andExpect(jsonPath("$.data.totalCustomers").value(1200))
                .andExpect(jsonPath("$.data.conversionRate").value(3.2))
                .andExpect(jsonPath("$.data.averageOrderValue").value(277.78));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    void getDashboardAnalytics_WithRegularUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/analytics/admin/dashboard")
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getDashboardAnalytics_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/analytics/admin/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void trackEvent_WithComplexMetadata_ShouldTrackSuccessfully() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        TrackEventRequestDto requestDto = new TrackEventRequestDto();
        requestDto.setEventType("PURCHASE_COMPLETE");
        requestDto.setOrderId(123L);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("total_amount", 299.99);
        metadata.put("payment_method", "credit_card");
        metadata.put("items_count", 3);
        metadata.put("discount_applied", true);
        requestDto.setMetadata(metadata);

        TrackEventResponse response = TrackEventResponse.success(3L, "PURCHASE_COMPLETE");
        when(trackEventUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/analytics/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void trackEvent_WithAllOptionalFields_ShouldTrackSuccessfully() throws Exception {
        // Given
        when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(mockUser));
        
        TrackEventRequestDto requestDto = new TrackEventRequestDto();
        requestDto.setEventType("CART_ABANDON");
        requestDto.setProductId(5L);
        requestDto.setOrderId(null);
        requestDto.setCategory("Fashion");
        requestDto.setPage("/cart");
        requestDto.setReferrer("/products/5");

        TrackEventResponse response = TrackEventResponse.success(4L, "CART_ABANDON");
        when(trackEventUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/analytics/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}