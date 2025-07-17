package com.ecommerce.core.domain.user.entity;

import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.Password;
import com.ecommerce.shared.exception.BusinessException;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private Email email;
    private Password password;
    private String phone;
    private boolean isActive;
    private boolean isEmailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public User(String firstName, String lastName, Email email, Password password, String phone) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validatePhone(phone);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isActive = true;
        this.isEmailVerified = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void updateProfile(String firstName, String lastName, String phone) {
        validateName(firstName, "First name");
        validateName(lastName, "Last name");
        validatePhone(phone);

        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void verifyEmail() {
        this.isEmailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isAvailable() {
        return isActive;
    }

    // Private validation methods
    private void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException(fieldName + " cannot be empty");
        }
        if (name.length() > 50) {
            throw new BusinessException(fieldName + " cannot exceed 50 characters");
        }
    }

    private void validatePhone(String phone) {
        if (phone != null && !phone.matches("^\\+?[1-9]\\d{1,14}$")) {
            throw new BusinessException("Invalid phone number format");
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Email getEmail() { return email; }
    public Password getPassword() { return password; }
    public String getPhone() { return phone; }
    public boolean isActive() { return isActive; }
    public boolean isEmailVerified() { return isEmailVerified; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Package-private setters for repository use
    void setId(Long id) { this.id = id; }
    void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    void setActive(boolean active) { this.isActive = active; }
    void setEmailVerified(boolean emailVerified) { this.isEmailVerified = emailVerified; }
}
