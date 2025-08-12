package com.ecommerce.adapter.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackUserBehaviorRequestDto {
    
    @NotBlank(message = "Event type is required")
    private String eventType;
    
    private Long productId;
    private Long orderId;
    private String category;
    private String page;
    private String referrer;
    private Map<String, Object> metadata;
}