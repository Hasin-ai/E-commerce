package com.ecommerce.core.domain.analytics.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserActivity {
    private UUID id;
    private String userId;
    private List<Event> events;
    private LocalDateTime lastActivity;
}
