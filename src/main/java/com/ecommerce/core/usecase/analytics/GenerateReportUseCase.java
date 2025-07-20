package com.ecommerce.core.usecase.analytics;

import com.ecommerce.core.domain.analytics.entity.Report;
import com.ecommerce.core.domain.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateReportUseCase {
    private final AnalyticsRepository analyticsRepository;

    public Report execute(String reportType, Map<String, Object> params) {
        return analyticsRepository.generateReport(reportType, params);
    }
}
