package com.ecommerce.adapter.web.dto.request.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ClearCartRequestDto {

    @JsonProperty("userId")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("saveForLater")
    private Boolean saveForLater = false; // Move items to wishlist instead of deleting

    // Default constructor
    public ClearCartRequestDto() {}

    // Constructor
    public ClearCartRequestDto(Long userId, Boolean saveForLater) {
        this.userId = userId;
        this.saveForLater = saveForLater;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getSaveForLater() {
        return saveForLater;
    }

    public void setSaveForLater(Boolean saveForLater) {
        this.saveForLater = saveForLater;
    }
}
