package com.ecommerce.core.domain.user.valueobject;

public enum AddressType {
    HOME("Home"),
    WORK("Work"),
    BILLING("Billing"),
    SHIPPING("Shipping"),
    OTHER("Other");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isBilling() {
        return this == BILLING;
    }

    public boolean isShipping() {
        return this == SHIPPING;
    }
}
