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
    
    private String currency;

    @JsonProperty("categoryId")
    private Long categoryId;
    
    private String category;

    @JsonProperty("imageUrl")
    private String imageUrl;
    
    @JsonProperty("stockQuantity")
    private Integer stockQuantity;
    
    private boolean inStock;

    @JsonProperty("isActive")
    private boolean isActive;
    
    @JsonProperty("isFeatured")
    private boolean isFeatured;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
