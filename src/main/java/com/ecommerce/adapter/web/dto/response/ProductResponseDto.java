package com.ecommerce.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String sku;
    private BigDecimal price;
    
    @JsonProperty("discountPrice")
    private BigDecimal discountPrice;
    
    @JsonProperty("categoryId")
    private Long categoryId;
    
    @JsonProperty("imageUrl")
    private String imageUrl;
    
    @JsonProperty("stockQuantity")
    private Integer stockQuantity;
    
    private String currency;
    private String category; // Add category name field
    private boolean inStock; // Add inStock field for search results
    
    @JsonProperty("isActive")
    private boolean isActive;
    
    @JsonProperty("isFeatured")
    private boolean isFeatured;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
