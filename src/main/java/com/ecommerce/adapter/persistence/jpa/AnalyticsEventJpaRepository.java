package com.ecommerce.adapter.persistence.jpa;

import com.ecommerce.adapter.persistence.entity.AnalyticsEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsEventJpaRepository extends JpaRepository<AnalyticsEventEntity, Long> {
    List<AnalyticsEventEntity> findByEventTypeAndTimestampBetween(String eventType, LocalDateTime from, LocalDateTime to);
    List<AnalyticsEventEntity> findByUserIdAndTimestampBetween(Long userId, LocalDateTime from, LocalDateTime to);
}