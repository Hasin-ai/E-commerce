package com.ecommerce.core.usecase.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackEventResponse {
    private Long eventId;
    private String eventType;
    private String status;
    private String message;
    private LocalDateTime timestamp;

    public static TrackEventResponse success(Long eventId, String eventType) {
        return TrackEventResponse.builder()
                .eventId(eventId)
                .eventType(eventType)
                .status("SUCCESS")
                .message("Event tracked successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static TrackEventResponse failure(String eventType, String message) {
        return TrackEventResponse.builder()
                .eventType(eventType)
                .status("FAILED")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}