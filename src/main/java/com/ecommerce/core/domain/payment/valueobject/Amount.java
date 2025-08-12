package com.ecommerce.core.domain.payment.valueobject;

import com.ecommerce.shared.exception.BusinessException;

import java.math.BigDecimal;

public class Amount {
    private final BigDecimal amount;
    private final String currency;

    public Amount(BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Amount cannot be null or negative");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new BusinessException("Currency is required");
        }
        this.amount = amount;
        this.currency = currency.toUpperCase();
    }

    public Amount(BigDecimal amount, Currency currency) {
        this(amount, currency.getCode());
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Amount add(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new BusinessException("Cannot add amounts with different currencies");
        }
        return new Amount(this.amount.add(other.amount), this.currency);
    }

    public Amount subtract(Amount other) {
        if (!this.currency.equals(other.currency)) {
            throw new BusinessException("Cannot subtract amounts with different currencies");
        }
        return new Amount(this.amount.subtract(other.amount), this.currency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Amount amount1 = (Amount) obj;
        return amount.equals(amount1.amount) && currency.equals(amount1.currency);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
