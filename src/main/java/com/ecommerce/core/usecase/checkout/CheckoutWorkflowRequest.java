package com.ecommerce.core.usecase.checkout;

public class CheckoutWorkflowRequest {
    private Long userId;
    private AddressRequest shippingAddress;
    private AddressRequest billingAddress;
    private String paymentMethod;
    private String paymentMethodId;
    private String customerId;

    public CheckoutWorkflowRequest(Long userId, AddressRequest shippingAddress, AddressRequest billingAddress,
                                 String paymentMethod, String paymentMethodId, String customerId) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.billingAddress = billingAddress;
        this.paymentMethod = paymentMethod;
        this.paymentMethodId = paymentMethodId;
        this.customerId = customerId;
    }

    // Getters
    public Long getUserId() { return userId; }
    public AddressRequest getShippingAddress() { return shippingAddress; }
    public AddressRequest getBillingAddress() { return billingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public String getCustomerId() { return customerId; }

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