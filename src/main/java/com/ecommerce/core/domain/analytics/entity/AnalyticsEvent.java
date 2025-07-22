package com.ecommerce.core.domain.analytics.entity;

import com.ecommerce.core.domain.analytics.valueobject.EventType;
import com.ecommerce.shared.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Map;

public class AnalyticsEvent {
    private Long id;
    private Long userId;
    private String sessionId;
    private EventType eventType;
    private String eventName;
    private Map<String, Object> properties;
    private String userAgent;
    private String ipAddress;
    private String referrer;
    private LocalDateTime timestamp;

    public AnalyticsEvent(Long userId, String sessionId, EventType eventType,
                         String eventName, Map<String, Object> properties) {
        validateEventName(eventName);

        this.userId = userId;
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.eventName = eventName;
        this.properties = properties;
        this.timestamp = LocalDateTime.now();
    }

    public void addProperty(String key, Object value) {
        if (this.properties == null) {
            this.properties = new java.util.HashMap<>();
        }
        this.properties.put(key, value);
    }

    public void setUserContext(String userAgent, String ipAddress, String referrer) {
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.referrer = referrer;
    }

    private void validateEventName(String eventName) {
        if (eventName == null || eventName.trim().isEmpty()) {
            throw new ValidationException("Event name cannot be empty");
        }
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public EventType getEventType() { return eventType; }
    public String getEventName() { return eventName; }
    public Map<String, Object> getProperties() { return properties; }
    public String getUserAgent() { return userAgent; }
    public String getIpAddress() { return ipAddress; }
    public String getReferrer() { return referrer; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Package-private setters for persistence
    void setId(Long id) { this.id = id; }
}
