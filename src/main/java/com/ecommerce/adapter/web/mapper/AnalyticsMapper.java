package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.request.EventRequest;
import com.ecommerce.core.domain.analytics.entity.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AnalyticsMapper {

    public Event toDomain(EventRequest request) {
        return Event.builder()
                .id(UUID.randomUUID())
                .eventType(request.getEventType())
                .userId(request.getUserId())
                .sessionId(request.getSessionId())
                .details(request.getDetails())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
