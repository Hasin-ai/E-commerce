package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.analytics.TrackEventUseCase;
import com.ecommerce.core.usecase.analytics.GetAnalyticsDashboardUseCase;
import com.ecommerce.core.usecase.analytics.TrackEventRequest;
import com.ecommerce.core.usecase.analytics.TrackEventResponse;
import com.ecommerce.core.usecase.analytics.GetAnalyticsDashboardResponse;
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
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackEventUseCase trackEventUseCase;

    @MockBean
    private GetAnalyticsDashboardUseCase getAnalyticsDashboardUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private TrackEventRequest trackEventRequest;

    @BeforeEach
    void setUp() {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("productId", 1L);
        eventData.put("category", "Electronics");

        trackEventRequest = new TrackEventRequest();
        trackEventRequest.setEventType("product_view");
        trackEventRequest.setUserId(1L);
        trackEventRequest.setEventData(eventData);
    }

    @Test
    @DisplayName("Should track analytics event successfully")
    void shouldTrackAnalyticsEventSuccessfully() throws Exception {
        // Given
        TrackEventResponse response = new TrackEventResponse();
        response.setSuccess(true);
        response.setEventId(1L);
        response.setMessage("Event tracked successfully");

        when(trackEventUseCase.execute(any(TrackEventRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/analytics/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trackEventRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.eventId").value(1L))
                .andExpect(jsonPath("$.message").value("Event tracked successfully"));
    }

    @Test
    @DisplayName("Should get analytics dashboard")
    void shouldGetAnalyticsDashboard() throws Exception {
        // Given
        GetAnalyticsDashboardResponse response = new GetAnalyticsDashboardResponse();
        response.setSuccess(true);
        response.setTotalUsers(1000L);
        response.setTotalOrders(500L);
        response.setTotalRevenue(BigDecimal.valueOf(50000.00));

        when(getAnalyticsDashboardUseCase.execute(any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/analytics/dashboard")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.totalUsers").value(1000))
                .andExpect(jsonPath("$.totalOrders").value(500));
    }

    @Test
    @DisplayName("Should handle event tracking failure")
    void shouldHandleEventTrackingFailure() throws Exception {
        // Given
        TrackEventResponse response = new TrackEventResponse();
        response.setSuccess(false);
        response.setErrorMessage("Analytics service unavailable");

        when(trackEventUseCase.execute(any(TrackEventRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/analytics/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trackEventRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Analytics service unavailable"));
    }

    @Test
    @DisplayName("Should validate event request")
    void shouldValidateEventRequest() throws Exception {
        // Given
        TrackEventRequest invalidRequest = new TrackEventRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/analytics/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
