package com.ecommerce.core.domain.user.valueobject;

import com.ecommerce.shared.exception.ValidationException;

import java.util.regex.Pattern;

public class Password {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );

    private final String value;

    public Password(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be null or empty");
        }
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        if (password.length() > 128) {
            throw new ValidationException("Password too long. Maximum length is 128 characters");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new ValidationException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
        }
    }

    public String getValue() {
        return value;
    }

    public boolean matches(String plainPassword) {
        // This is a simple comparison - in a real application, you'd use BCrypt or similar
        return value.equals(plainPassword);
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
        return "********"; // Never expose password value
    }
}
