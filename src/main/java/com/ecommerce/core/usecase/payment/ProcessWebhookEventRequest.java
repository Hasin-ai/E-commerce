package com.ecommerce.core.usecase.payment;

public class ProcessWebhookEventRequest {
    private final String eventId;
    private final String eventType;
    private final String payload;
    private final Long createdTimestamp;

    public ProcessWebhookEventRequest(String eventId, String eventType, String payload, Long createdTimestamp) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdTimestamp = createdTimestamp;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }
}