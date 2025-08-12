package com.ecommerce.core.usecase.analytics;

import com.ecommerce.adapter.web.dto.response.AnalyticsDashboardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAnalyticsDashboardResponse {
    private AnalyticsDashboardDto dashboard;
}