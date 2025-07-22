package com.ecommerce.core.domain.analytics.repository;

import com.ecommerce.core.domain.analytics.entity.AnalyticsEvent;
import com.ecommerce.core.domain.analytics.valueobject.EventType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AnalyticsRepository {
    AnalyticsEvent save(AnalyticsEvent event);
    List<AnalyticsEvent> findByUserId(Long userId);
    List<AnalyticsEvent> findByEventType(EventType eventType);
    List<AnalyticsEvent> findBySessionId(String sessionId);
    List<AnalyticsEvent> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    Map<EventType, Long> getEventCountsByType(LocalDateTime start, LocalDateTime end);
    List<Map<String, Object>> getDailyActiveUsers(LocalDateTime start, LocalDateTime end);
    List<Map<String, Object>> getTopProducts(LocalDateTime start, LocalDateTime end, int limit);
}
