package com.ecommerce.core.domain.notification.valueobject;

public enum NotificationType {
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Push Notification"),
    IN_APP("In-App Notification"),
    WEBHOOK("Webhook");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean requiresRecipient() {
        return this == EMAIL || this == SMS || this == PUSH;
    }

    public boolean isRealTime() {
        return this == PUSH || this == IN_APP || this == WEBHOOK;
    }
}
