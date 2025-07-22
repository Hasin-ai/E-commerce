package com.ecommerce.core.domain.cart.valueobject;

import com.ecommerce.shared.exception.BusinessException;

public class Quantity {
    private final int value;

    public Quantity(int value) {
        if (value <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }
        if (value > 999) {
            throw new BusinessException("Quantity cannot exceed 999");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }

    public Quantity subtract(Quantity other) {
        return new Quantity(this.value - other.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quantity quantity = (Quantity) obj;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
