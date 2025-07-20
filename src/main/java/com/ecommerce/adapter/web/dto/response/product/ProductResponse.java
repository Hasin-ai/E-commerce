package com.ecommerce.adapter.web.dto.response.product;

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
public class ProductResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String sku;

    @JsonProperty("basePrice")
    private BigDecimal basePrice;

    @JsonProperty("salePrice")
    private BigDecimal salePrice;

    private String currency;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("isFeatured")
    private boolean isFeatured;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
