package com.ecommerce.adapter.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDashboardDto {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalCustomers;
    private Long newCustomers;
    private BigDecimal averageOrderValue;
    private BigDecimal conversionRate;
    private List<SalesMetricsDto> salesTrend;
    private List<Map<String, Object>> topProducts;
    private List<Map<String, Object>> topCategories;
    private Map<String, Long> customerSegments;
}