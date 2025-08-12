package com.ecommerce.core.domain.notification.valueobject;

public enum NotificationStatus {
    PENDING("Pending"),
    SCHEDULED("Scheduled"),
    SENT("Sent"),
    DELIVERED("Delivered"),
    FAILED("Failed"),
    READ("Read"),
    CANCELLED("Cancelled");

    private final String displayName;

    NotificationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isFinal() {
        return this == DELIVERED || this == READ || this == CANCELLED;
    }

    public boolean canRetry() {
        return this == FAILED;
    }
}
