package com.ecommerce.core.domain.user.entity;

import com.ecommerce.core.domain.user.valueobject.AddressType;

import java.time.LocalDateTime;

public class UserAddress {
    private Long id;
    private Long userId;
    private String street;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private AddressType type;
    private boolean isDefault;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public UserAddress() {
        this.isDefault = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with required fields
    public UserAddress(Long userId, String street, String city, String state,
                      String country, String postalCode, AddressType type) {
        this();
        this.userId = userId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.type = type;
    }

    // Business methods
    public void setAsDefault() {
        this.isDefault = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void unsetAsDefault() {
        this.isDefault = false;
        this.updatedAt = LocalDateTime.now();
    }

    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (street != null) address.append(street).append(", ");
        if (city != null) address.append(city).append(", ");
        if (state != null) address.append(state).append(", ");
        if (country != null) address.append(country);
        if (postalCode != null) address.append(" ").append(postalCode);
        return address.toString();
    }

    public boolean isComplete() {
        return street != null && !street.trim().isEmpty() &&
               city != null && !city.trim().isEmpty() &&
               country != null && !country.trim().isEmpty() &&
               postalCode != null && !postalCode.trim().isEmpty();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        this.updatedAt = LocalDateTime.now();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
        this.updatedAt = LocalDateTime.now();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        this.updatedAt = LocalDateTime.now();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
        this.updatedAt = LocalDateTime.now();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
        this.updatedAt = LocalDateTime.now();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        this.updatedAt = LocalDateTime.now();
    }

    public AddressType getType() {
        return type;
    }

    public void setType(AddressType type) {
        this.type = type;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
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
}
