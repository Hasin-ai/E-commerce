package com.ecommerce.core.domain.analytics.entity;

import java.time.LocalDateTime;
import java.util.Map;

public class UserEvent {
    private Long id;
    private Long userId;
    private String sessionId;
    private String eventType;
    private Map<String, Object> eventData;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userAgent;
    private String deviceType;
    private String platform;

    public UserEvent() {}

    public UserEvent(Long userId, String sessionId, Object eventType, Map<String, Object> eventData,
                    LocalDateTime timestamp, String ipAddress, String userAgent,
                    Object deviceType, String platform) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.eventType = eventType != null ? eventType.toString() : null;
        this.eventData = eventData;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.deviceType = deviceType != null ? deviceType.toString() : null;
        this.platform = platform;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Map<String, Object> getEventData() { return eventData; }
    public void setEventData(Map<String, Object> eventData) { this.eventData = eventData; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
}
