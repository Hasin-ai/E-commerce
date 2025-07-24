package com.ecommerce.core.domain.analytics.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEvent {
    private Long id;
    private String eventType;
    private Long userId;
    private String sessionId;
    private Map<String, Object> properties;
    private String userAgent;
    private String ipAddress;
    private LocalDateTime timestamp;
}