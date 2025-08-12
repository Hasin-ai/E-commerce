package com.ecommerce.core.usecase.order;

public class CreateOrderRequest {
    private Long userId;
    private AddressRequest shippingAddress;
    private AddressRequest billingAddress;

    public CreateOrderRequest(Long userId, AddressRequest shippingAddress, AddressRequest billingAddress) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
    }

    // Getters
    public Long getUserId() { return userId; }
    public AddressRequest getShippingAddress() { return shippingAddress; }
    public AddressRequest getBillingAddress() { return billingAddress; }

    public static class AddressRequest {
        private String street;
        private String city;
        private String state;
        private String zipCode;
        private String country;

        public AddressRequest(String street, String city, String state, String zipCode, String country) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.country = country;
        }

        // Getters
        public String getStreet() { return street; }
        public String getCity() { return city; }
        public String getState() { return state; }
        public String getZipCode() { return zipCode; }
        public String getCountry() { return country; }
    }
}