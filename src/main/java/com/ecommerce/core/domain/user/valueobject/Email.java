package com.ecommerce.core.domain.user.valueobject;

import com.ecommerce.shared.exception.ValidationException;

public class Email {
    private final String value;

    public Email(String value) {
        validate(value);
        this.value = value.toLowerCase();
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!value.matches(emailRegex)) {
            throw new ValidationException("Invalid email format");
        }

        if (value.length() > 255) {
            throw new ValidationException("Email cannot exceed 255 characters");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Email email = (Email) obj;
        return value.equals(email.value);
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
