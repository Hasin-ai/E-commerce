package com.ecommerce.core.usecase.user;

import java.time.LocalDateTime;

public class RegisterUserResponse {

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final boolean isActive;
    private final boolean isEmailVerified;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public RegisterUserResponse(Long id, String firstName, String lastName, String email, String phone,
                               boolean isActive, boolean isEmailVerified, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.isActive = isActive;
        this.isEmailVerified = isEmailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}