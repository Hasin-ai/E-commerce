package com.ecommerce.core.domain.order.valueobject;

import com.ecommerce.shared.exception.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Price {
    private final BigDecimal amount;
    private final String currency;

    public Price(BigDecimal amount, String currency) {
        validate(amount, currency);
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase();
    }

    public Price(String amount, String currency) {
        this(new BigDecimal(amount), currency);
    }

    private void validate(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new ValidationException("Price amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price cannot be negative");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new ValidationException("Currency cannot be empty");
        }
        if (!currency.matches("^[A-Z]{3}$")) {
            throw new ValidationException("Currency must be a valid 3-letter code");
        }
    }

    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public Price add(Price other) {
        if (!this.currency.equals(other.currency)) {
            throw new ValidationException("Cannot add prices with different currencies");
        }
        return new Price(this.amount.add(other.amount), this.currency);
    }

    public Price multiply(int quantity) {
        return new Price(this.amount.multiply(BigDecimal.valueOf(quantity)), this.currency);
    }

    // Getters
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Price price = (Price) obj;
        return amount.equals(price.amount) && currency.equals(price.currency);
    }

    @Override
    public int hashCode() {
        return amount.hashCode() + currency.hashCode();
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
