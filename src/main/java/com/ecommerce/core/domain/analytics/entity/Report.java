package com.ecommerce.core.domain.analytics.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class Report {
    private UUID id;
    private String reportType;
    private Map<String, Object> results;
    private LocalDateTime generatedAt;
}
