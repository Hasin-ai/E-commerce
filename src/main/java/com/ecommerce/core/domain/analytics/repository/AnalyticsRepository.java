package com.ecommerce.core.domain.analytics.repository;

import com.ecommerce.core.domain.analytics.entity.AnalyticsEvent;
import com.ecommerce.core.domain.analytics.entity.SalesMetrics;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsRepository {
    AnalyticsEvent trackEvent(AnalyticsEvent event);
    AnalyticsEvent save(AnalyticsEvent event);
    List<AnalyticsEvent> findEventsByType(String eventType, LocalDate from, LocalDate to);
    List<AnalyticsEvent> findEventsByUser(Long userId, LocalDate from, LocalDate to);
    
    SalesMetrics getSalesMetrics(LocalDate date);
    List<SalesMetrics> getSalesMetrics(LocalDate from, LocalDate to);
    
    List<Object[]> getTopProducts(LocalDate from, LocalDate to, int limit);
    List<Object[]> getTopCategories(LocalDate from, LocalDate to, int limit);
    List<Object[]> getCustomerSegments();
}