package com.ecommerce.core.domain.product.valueobject;

import com.ecommerce.shared.exception.ValidationException;
import java.math.BigDecimal;

public class Price {
    private final BigDecimal amount;
    private final String currency;

    public Price(BigDecimal amount, String currency) {
        validate(amount, currency);
        this.amount = amount;
        this.currency = currency.toUpperCase();
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
        if (currency.length() != 3) {
            throw new ValidationException("Currency must be 3 characters (ISO 4217)");
        }
    }

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
}
