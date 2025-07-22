package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateWishlistRequestDto {

    @JsonProperty("userId")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("name")
    @NotBlank(message = "Wishlist name is required")
    @Size(min = 1, max = 100, message = "Wishlist name must be between 1 and 100 characters")
    private String name;

    @JsonProperty("description")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @JsonProperty("isPublic")
    private Boolean isPublic = false;

    @JsonProperty("productIds")
    private List<Long> productIds;

    // Default constructor
    public CreateWishlistRequestDto() {}

    // Constructor
    public CreateWishlistRequestDto(Long userId, String name, String description,
                                   Boolean isPublic, List<Long> productIds) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.productIds = productIds;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
