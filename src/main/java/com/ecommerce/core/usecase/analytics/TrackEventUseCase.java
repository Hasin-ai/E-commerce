package com.ecommerce.core.usecase.analytics;

import com.ecommerce.core.domain.analytics.entity.AnalyticsEvent;
import com.ecommerce.core.domain.analytics.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TrackEventUseCase {
    
    private final AnalyticsRepository analyticsRepository;
    
    public TrackEventResponse execute(TrackEventRequest request) {
        try {
            AnalyticsEvent event = AnalyticsEvent.builder()
                    .eventType(request.getEventType())
                    .userId(request.getUserId())
                    .sessionId(request.getSessionId())
                    .properties(request.getProperties())
                    .userAgent(request.getUserAgent())
                    .ipAddress(request.getIpAddress())
                    .timestamp(LocalDateTime.now())
                    .build();
            
            AnalyticsEvent savedEvent = analyticsRepository.trackEvent(event);
            return TrackEventResponse.success(savedEvent.getId(), request.getEventType());
            
        } catch (Exception e) {
            return TrackEventResponse.failure(request.getEventType(), 
                "Failed to track event: " + e.getMessage());
        }
    }
}