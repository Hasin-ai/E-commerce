package com.ecommerce.core.domain.order.entity;

import com.ecommerce.shared.exception.ValidationException;

public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Address(String street, String city, String state, String zipCode, String country) {
        validateStreet(street);
        validateCity(city);
        validateState(state);
        validateZipCode(zipCode);
        validateCountry(country);

        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    private void validateStreet(String street) {
        if (street == null || street.trim().isEmpty()) {
            throw new ValidationException("Street address cannot be empty");
        }
    }

    private void validateCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new ValidationException("City cannot be empty");
        }
    }

    private void validateState(String state) {
        if (state == null || state.trim().isEmpty()) {
            throw new ValidationException("State cannot be empty");
        }
    }

    private void validateZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new ValidationException("Zip code cannot be empty");
        }
    }

    private void validateCountry(String country) {
        if (country == null || country.trim().isEmpty()) {
            throw new ValidationException("Country cannot be empty");
        }
    }

    // Getters
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
}