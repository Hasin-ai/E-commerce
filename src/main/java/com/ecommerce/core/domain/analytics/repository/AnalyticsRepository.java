package com.ecommerce.core.domain.analytics.repository;

import com.ecommerce.core.domain.analytics.entity.Event;
import com.ecommerce.core.domain.analytics.entity.Report;
import com.ecommerce.core.domain.analytics.entity.UserActivity;

import java.util.Optional;

public interface AnalyticsRepository {
    void saveEvent(Event event);

    Optional<UserActivity> findUserActivityByUserId(String userId);

    Report generateReport(String reportType, java.util.Map<String, Object> params);
}
