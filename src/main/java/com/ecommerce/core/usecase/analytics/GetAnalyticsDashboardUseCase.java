package com.ecommerce.core.usecase.analytics;

import com.ecommerce.adapter.web.dto.response.AnalyticsDashboardDto;
import com.ecommerce.adapter.web.dto.response.SalesMetricsDto;
import com.ecommerce.core.domain.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAnalyticsDashboardUseCase {
    
    private final AnalyticsRepository analyticsRepository;
    
    public GetAnalyticsDashboardResponse execute(LocalDate from, LocalDate to) {
        var salesMetrics = analyticsRepository.getSalesMetrics(from, to);
        var topProducts = analyticsRepository.getTopProducts(from, to, 10);
        var topCategories = analyticsRepository.getTopCategories(from, to, 10);
        var customerSegments = analyticsRepository.getCustomerSegments();
        
        // Convert to DTOs
        List<SalesMetricsDto> salesTrend = salesMetrics.stream()
                .map(metrics -> SalesMetricsDto.builder()
                        .date(metrics.getDate())
                        .totalRevenue(metrics.getTotalRevenue())
                        .totalOrders(metrics.getTotalOrders())
                        .averageOrderValue(metrics.getAverageOrderValue())
                        .totalCustomers(metrics.getTotalCustomers())
                        .newCustomers(metrics.getNewCustomers())
                        .conversionRate(metrics.getConversionRate())
                        .build())
                .collect(Collectors.toList());
        
        // Calculate totals
        var totals = salesMetrics.stream()
                .reduce((a, b) -> com.ecommerce.core.domain.analytics.entity.SalesMetrics.builder()
                        .totalRevenue(a.getTotalRevenue().add(b.getTotalRevenue()))
                        .totalOrders(a.getTotalOrders() + b.getTotalOrders())
                        .totalCustomers(Math.max(a.getTotalCustomers(), b.getTotalCustomers()))
                        .newCustomers(a.getNewCustomers() + b.getNewCustomers())
                        .build())
                .orElse(com.ecommerce.core.domain.analytics.entity.SalesMetrics.builder().build());
        
        AnalyticsDashboardDto dashboard = AnalyticsDashboardDto.builder()
                .totalRevenue(totals.getTotalRevenue())
                .totalOrders(totals.getTotalOrders())
                .totalCustomers(totals.getTotalCustomers())
                .newCustomers(totals.getNewCustomers())
                .averageOrderValue(totals.getAverageOrderValue())
                .conversionRate(totals.getConversionRate())
                .salesTrend(salesTrend)
                .topProducts(convertToMap(topProducts))
                .topCategories(convertToMap(topCategories))
                .customerSegments(convertCustomerSegments(customerSegments))
                .build();
        
        return GetAnalyticsDashboardResponse.builder()
                .dashboard(dashboard)
                .build();
    }
    
    private List<Map<String, Object>> convertToMap(List<Object[]> data) {
        return data.stream()
                .map(row -> Map.of(
                        "name", row[0],
                        "value", row[1]
                ))
                .collect(Collectors.toList());
    }
    
    private Map<String, Long> convertCustomerSegments(List<Object[]> data) {
        return data.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                ));
    }
}