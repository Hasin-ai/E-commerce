package com.ecommerce.core.domain.user.valueobject;

import com.ecommerce.shared.exception.ValidationException;

import java.util.regex.Pattern;

public class Phone {
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^\\+?[1-9]\\d{1,14}$" // E.164 format
    );

    private final String value;

    public Phone(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ValidationException("Phone number cannot be null or empty");
        }
        
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        
        if (!PHONE_PATTERN.matcher(cleanPhone).matches()) {
            throw new ValidationException("Invalid phone number format: " + phone);
        }
        
        if (cleanPhone.length() > 15) {
            throw new ValidationException("Phone number too long. Maximum length is 15 digits");
        }
    }

    public String getValue() {
        return value;
    }

    public String getFormattedValue() {
        // Simple formatting - in real app, use libphonenumber
        if (value.startsWith("+1") && value.length() == 12) {
            return String.format("+1 (%s) %s-%s", 
                value.substring(2, 5), 
                value.substring(5, 8), 
                value.substring(8));
        }
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Phone phone = (Phone) obj;
        return value.equals(phone.value);
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