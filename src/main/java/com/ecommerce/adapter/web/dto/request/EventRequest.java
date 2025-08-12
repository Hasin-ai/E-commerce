package com.ecommerce.adapter.web.dto.request;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class EventRequest {
    @NotEmpty
    private String eventType;
    private String userId;
    private String sessionId;
    private Map<String, String> details;
}
