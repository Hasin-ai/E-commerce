package com.ecommerce.core.domain.analytics.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class Event {
    private UUID id;
    private String eventType;
    private String userId;
    private String sessionId;
    private Map<String, String> details;
    private LocalDateTime timestamp;
}
