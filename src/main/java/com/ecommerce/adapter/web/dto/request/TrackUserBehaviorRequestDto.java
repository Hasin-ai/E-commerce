package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class TrackUserBehaviorRequestDto {

    @JsonProperty("userId")
    private Long userId; // Can be null for anonymous users

    @JsonProperty("sessionId")
    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @JsonProperty("eventType")
    @NotBlank(message = "Event type is required")
    private String eventType; // PAGE_VIEW, PRODUCT_VIEW, ADD_TO_CART, PURCHASE, SEARCH, etc.

    @JsonProperty("eventData")
    private Map<String, Object> eventData; // Additional event-specific data

    @JsonProperty("productId")
    private Long productId; // For product-related events

    @JsonProperty("categoryId")
    private Long categoryId; // For category-related events

    @JsonProperty("timestamp")
    @NotNull(message = "Timestamp is required")
    private Long timestamp; // Unix timestamp

    @JsonProperty("userAgent")
    private String userAgent;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("referrer")
    private String referrer;

    @JsonProperty("page")
    private String page; // Current page/route

    // Default constructor
    public TrackUserBehaviorRequestDto() {}

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map<String, Object> getEventData() {
        return eventData;
    }

    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
