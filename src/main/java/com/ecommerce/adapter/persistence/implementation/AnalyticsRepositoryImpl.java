package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.entity.AnalyticsEventEntity;
import com.ecommerce.adapter.persistence.jpa.AnalyticsEventJpaRepository;
import com.ecommerce.adapter.persistence.mapper.AnalyticsMapper;
import com.ecommerce.core.domain.analytics.entity.AnalyticsEvent;
import com.ecommerce.core.domain.analytics.entity.SalesMetrics;
import com.ecommerce.core.domain.analytics.repository.AnalyticsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnalyticsRepositoryImpl implements AnalyticsRepository {
    
    private final AnalyticsEventJpaRepository jpaRepository;
    private final AnalyticsMapper mapper;
    private final ObjectMapper objectMapper;
    
    @Override
    public void trackEvent(AnalyticsEvent event) {
        AnalyticsEventEntity entity = mapper.toEntity(event);
        
        // Convert properties map to JSON string
        if (event.getProperties() != null) {
            try {
                entity.setProperties(objectMapper.writeValueAsString(event.getProperties()));
            } catch (JsonProcessingException e) {
                entity.setProperties("{}");
            }
        }
        
        jpaRepository.save(entity);
    }
    
    @Override
    public List<AnalyticsEvent> findEventsByType(String eventType, LocalDate from, LocalDate to) {
        return jpaRepository.findByEventTypeAndTimestampBetween(eventType, from.atStartOfDay(), to.atTime(23, 59, 59))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<AnalyticsEvent> findEventsByUser(Long userId, LocalDate from, LocalDate to) {
        return jpaRepository.findByUserIdAndTimestampBetween(userId, from.atStartOfDay(), to.atTime(23, 59, 59))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public SalesMetrics getSalesMetrics(LocalDate date) {
        // This would typically query order/payment tables
        // For now, return mock data
        return SalesMetrics.builder()
                .date(date)
                .totalRevenue(BigDecimal.valueOf(10000))
                .totalOrders(100L)
                .averageOrderValue(BigDecimal.valueOf(100))
                .totalCustomers(50L)
                .newCustomers(10L)
                .conversionRate(BigDecimal.valueOf(0.05))
                .build();
    }
    
    @Override
    public List<SalesMetrics> getSalesMetrics(LocalDate from, LocalDate to) {
        // This would typically query order/payment tables
        // For now, return mock data
        return List.of(getSalesMetrics(from), getSalesMetrics(to));
    }
    
    @Override
    public List<Object[]> getTopProducts(LocalDate from, LocalDate to, int limit) {
        // This would typically query product sales data
        return List.of(
                new Object[]{"Product A", 100L},
                new Object[]{"Product B", 80L},
                new Object[]{"Product C", 60L}
        );
    }
    
    @Override
    public List<Object[]> getTopCategories(LocalDate from, LocalDate to, int limit) {
        // This would typically query category sales data
        return List.of(
                new Object[]{"Electronics", 200L},
                new Object[]{"Clothing", 150L},
                new Object[]{"Books", 100L}
        );
    }
    
    @Override
    public List<Object[]> getCustomerSegments() {
        // This would typically query customer data
        return List.of(
                new Object[]{"New Customers", 100L},
                new Object[]{"Returning Customers", 200L},
                new Object[]{"VIP Customers", 50L}
        );
    }
}