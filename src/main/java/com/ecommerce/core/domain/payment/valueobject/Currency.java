package com.ecommerce.core.domain.payment.valueobject;

import com.ecommerce.shared.exception.BusinessException;

import java.util.Set;

public class Currency {
    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP", "CAD", "AUD", "JPY");
    
    private final String code;
    
    public Currency(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new BusinessException("Currency code is required");
        }
        
        String upperCode = code.trim().toUpperCase();
        if (!SUPPORTED_CURRENCIES.contains(upperCode)) {
            throw new BusinessException("Unsupported currency: " + upperCode);
        }
        
        this.code = upperCode;
    }
    
    public String getCode() {
        return code;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Currency currency = (Currency) obj;
        return code.equals(currency.code);
    }
    
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return code;
    }
}
