package com.ecommerce.core.domain.user.valueobject;

import com.ecommerce.shared.exception.ValidationException;

public class Password {
    private final String value;

    public Password(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty");
        }

        if (value.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        if (value.length() > 128) {
            throw new ValidationException("Password cannot exceed 128 characters");
        }

        // Check for at least one digit, one lowercase, one uppercase, and one special character
        if (!value.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit");
        }

        if (!value.matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain at least one lowercase letter");
        }

        if (!value.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter");
        }

        if (!value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new ValidationException("Password must contain at least one special character");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Password password = (Password) obj;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "********"; // Never expose the actual password
    }
}
