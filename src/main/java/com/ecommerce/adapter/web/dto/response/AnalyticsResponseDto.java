package com.ecommerce.adapter.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponseDto {

    // Dashboard analytics fields
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalUsers;
    private Double conversionRate;
    private BigDecimal averageOrderValue;

    // Event analytics fields
    @JsonProperty("id")
    private Long id;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("sessionId")
    private String sessionId;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("eventData")
    private Map<String, Object> eventData;

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("categoryId")
    private Long categoryId;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("referrer")
    private String referrer;

    @JsonProperty("page")
    private String page;

    @JsonProperty("processed")
    private Boolean processed;

    @JsonProperty("processedAt")
    private LocalDateTime processedAt;


}
