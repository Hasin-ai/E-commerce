package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.analytics.entity.Event;
import com.ecommerce.adapter.persistence.jpa.entity.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEntityMapper {

    public EventEntity toEntity(Event domain) {
        if (domain == null) {
            return null;
        }
        EventEntity entity = new EventEntity();
        entity.setId(domain.getId());
        entity.setEventType(domain.getEventType());
        entity.setUserId(domain.getUserId());
        entity.setSessionId(domain.getSessionId());
        entity.setDetailsAsMap(domain.getDetails());
        entity.setTimestamp(domain.getTimestamp());
        return entity;
    }

    public Event toDomain(EventEntity entity) {
        if (entity == null) {
            return null;
        }
        return Event.builder()
                .id(entity.getId())
                .eventType(entity.getEventType())
                .userId(entity.getUserId())
                .sessionId(entity.getSessionId())
                .details(entity.getDetailsAsMap())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
