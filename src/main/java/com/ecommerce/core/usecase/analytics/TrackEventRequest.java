package com.ecommerce.core.usecase.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackEventRequest {
    private String eventType;
    private Long userId;
    private String sessionId;
    private Map<String, Object> properties;
    private String userAgent;
    private String ipAddress;
}