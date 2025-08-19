package com.ecommerce.adapter.web.controller;

import com.ecommerce.core.usecase.inventory.UpdateInventoryUseCase;
import com.ecommerce.core.usecase.inventory.UpdateInventoryRequest;
import com.ecommerce.core.usecase.inventory.UpdateInventoryResponse;
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

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpdateInventoryUseCase updateInventoryUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private UpdateInventoryRequest updateRequest;

    @BeforeEach
    void setUp() {
        updateRequest = new UpdateInventoryRequest();
        updateRequest.setProductId(1L);
        updateRequest.setQuantity(50);
        updateRequest.setOperation("ADD");
    }

    @Test
    @DisplayName("Should update inventory successfully")
    void shouldUpdateInventorySuccessfully() throws Exception {
        // Given
        UpdateInventoryResponse response = new UpdateInventoryResponse();
        response.setSuccess(true);
        response.setProductId(1L);
        response.setNewQuantity(150);
        response.setMessage("Inventory updated successfully");

        when(updateInventoryUseCase.execute(any(UpdateInventoryRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.productId").value(1L))
                .andExpect(jsonPath("$.newQuantity").value(150));
    }

    @Test
    @DisplayName("Should get inventory by product ID")
    void shouldGetInventoryByProductId() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/inventory/product/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle inventory update failure")
    void shouldHandleInventoryUpdateFailure() throws Exception {
        // Given
        UpdateInventoryResponse response = new UpdateInventoryResponse();
        response.setSuccess(false);
        response.setErrorMessage("Insufficient inventory");

        when(updateInventoryUseCase.execute(any(UpdateInventoryRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(put("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Insufficient inventory"));
    }

    @Test
    @DisplayName("Should validate inventory update request")
    void shouldValidateInventoryUpdateRequest() throws Exception {
        // Given
        UpdateInventoryRequest invalidRequest = new UpdateInventoryRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(put("/api/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
