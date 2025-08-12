package com.ecommerce.adapter.web.dto.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCategoryRequestDto {

    @JsonProperty("name")
    @NotBlank(message = "Category name is required")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String name;

    @JsonProperty("description")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @JsonProperty("parentCategoryId")
    private Long parentCategoryId;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("isActive")
    private Boolean isActive = true;

    @JsonProperty("sortOrder")
    private Integer sortOrder;

    // Default constructor
    public CreateCategoryRequestDto() {}

    // Constructor
    public CreateCategoryRequestDto(String name, String description, Long parentCategoryId,
                                   String imageUrl, Boolean isActive, Integer sortOrder) {
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.sortOrder = sortOrder;
    }

    // Getters and Setters
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

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
