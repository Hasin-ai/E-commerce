package com.ecommerce.core.domain.user.entity;

import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.Password;
import com.ecommerce.core.domain.user.valueobject.UserRole;
import com.ecommerce.core.domain.user.valueobject.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

public class User {
    private Long id;
    private Email email;
    private Password password;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private UserStatus status;
    private boolean emailVerified;
    private String verificationToken;
    private String resetPasswordToken;
    private LocalDateTime resetPasswordTokenExpiry;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<UserAddress> addresses;
    private UserProfile profile;

    // Default constructor
    public User() {
        this.status = UserStatus.PENDING_VERIFICATION;
        this.role = UserRole.CUSTOMER;
        this.emailVerified = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with required fields
    public User(String email, String password) {
        this();
        this.email = new Email(email);
        this.password = new Password(password);
    }

    // Constructor with all basic fields
    public User(String email, String password, String firstName, String lastName, String phone) {
        this(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    // Business methods
    public void verifyEmail() {
        this.emailVerified = true;
        this.status = UserStatus.ACTIVE;
        this.verificationToken = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void activateAccount() {
        this.status = UserStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void suspendAccount() {
        this.status = UserStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivateAccount() {
        this.status = UserStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void setResetPasswordToken(String token, LocalDateTime expiry) {
        this.resetPasswordToken = token;
        this.resetPasswordTokenExpiry = expiry;
        this.updatedAt = LocalDateTime.now();
    }

    public void clearResetPasswordToken() {
        this.resetPasswordToken = null;
        this.resetPasswordTokenExpiry = null;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isResetTokenValid() {
        return resetPasswordToken != null &&
               resetPasswordTokenExpiry != null &&
               resetPasswordTokenExpiry.isAfter(LocalDateTime.now());
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public void setEmail(String email) {
        this.email = new Email(email);
        this.updatedAt = LocalDateTime.now();
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPassword(String password) {
        this.password = new Password(password);
        this.updatedAt = LocalDateTime.now();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.updatedAt = LocalDateTime.now();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.updatedAt = LocalDateTime.now();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        this.updatedAt = LocalDateTime.now();
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        this.updatedAt = LocalDateTime.now();
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public LocalDateTime getResetPasswordTokenExpiry() {
        return resetPasswordTokenExpiry;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<UserAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<UserAddress> addresses) {
        this.addresses = addresses;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
