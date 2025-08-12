package com.ecommerce.core.domain.user.valueobject;

public enum UserRole {
    CUSTOMER("Customer"),
    ADMIN("Administrator"),
    MERCHANT("Merchant"),
    SUPPORT("Support Staff");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isCustomer() {
        return this == CUSTOMER;
    }

    public boolean isMerchant() {
        return this == MERCHANT;
    }

    public boolean isSupport() {
        return this == SUPPORT;
    }
}
