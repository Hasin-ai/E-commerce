package com.ecommerce.core.domain.analytics.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class Report {
    private UUID id;
    private String reportType;
    private Map<String, Object> results;
    private LocalDateTime generatedAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private Map<String, Object> parameters;

    public Report(UUID id, String reportType, Map<String, Object> results, LocalDateTime generatedAt) {
        this.id = id;
        this.reportType = reportType;
        this.results = results;
        this.generatedAt = generatedAt;
    }

    public Report(String reportType, LocalDate startDate, LocalDate endDate, Map<String, Object> results, Map<String, Object> parameters) {
        this.id = UUID.randomUUID();
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.results = results;
        this.parameters = parameters;
        this.generatedAt = LocalDateTime.now();
    }

    // Constructor for full report with all parameters
    public Report(UUID id, String reportType, Map<String, Object> results, LocalDateTime generatedAt,
                 LocalDate startDate, LocalDate endDate, Map<String, Object> parameters) {
        this.id = id;
        this.reportType = reportType;
        this.results = results;
        this.generatedAt = generatedAt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.parameters = parameters;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
