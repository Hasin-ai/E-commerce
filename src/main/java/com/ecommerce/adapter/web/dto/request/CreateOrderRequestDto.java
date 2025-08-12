package com.ecommerce.adapter.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequestDto {

    @Valid
    @NotNull(message = "Shipping address is required")
    @JsonProperty("shippingAddress")
    private AddressDto shippingAddress;

    @Valid
    @NotNull(message = "Billing address is required")
    @JsonProperty("billingAddress")
    private AddressDto billingAddress;

    // Constructors
    public CreateOrderRequestDto() {}

    // Getters and setters
    public AddressDto getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(AddressDto shippingAddress) { this.shippingAddress = shippingAddress; }

    public AddressDto getBillingAddress() { return billingAddress; }
    public void setBillingAddress(AddressDto billingAddress) { this.billingAddress = billingAddress; }

    public static class AddressDto {
        @NotNull(message = "Street is required")
        private String street;

        @NotNull(message = "City is required")
        private String city;

        @NotNull(message = "State is required")
        private String state;

        @NotNull(message = "Zip code is required")
        @JsonProperty("zipCode")
        private String zipCode;

        @NotNull(message = "Country is required")
        private String country;

        // Constructors
        public AddressDto() {}

        // Getters and setters
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getState() { return state; }
        public void setState(String state) { this.state = state; }

        public String getZipCode() { return zipCode; }
        public void setZipCode(String zipCode) { this.zipCode = zipCode; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }
}