package com.ecommerce.core.domain.user.valueobject;

public enum UserStatus {
    PENDING_VERIFICATION("Pending Verification"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    DELETED("Deleted");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canLogin() {
        return this == ACTIVE;
    }

    public boolean isPendingVerification() {
        return this == PENDING_VERIFICATION;
    }

    public boolean isSuspended() {
        return this == SUSPENDED;
    }
}
