package com.ecommerce.core.domain.product.valueobject;

import com.ecommerce.shared.exception.ValidationException;

public class SKU {
    private final String value;

    public SKU(String value) {
        validate(value);
        this.value = value.toUpperCase();
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("SKU cannot be empty");
        }
        if (!value.matches("^[A-Z0-9-]{3,20}$")) {
            throw new ValidationException("SKU must be 3-20 characters, alphanumeric and hyphens only");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SKU sku = (SKU) obj;
        return value.equals(sku.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
