package com.ecommerce.adapter.web.dto.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class ProcessPaymentRequestDto {

    @JsonProperty("paymentIntentId")
    @NotBlank(message = "Payment intent ID is required")
    private String paymentIntentId;

    @JsonProperty("paymentMethod")
    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @JsonProperty("cardLastFour")
    private String cardLastFour;

    @JsonProperty("cardBrand")
    private String cardBrand;

    // Default constructor
    public ProcessPaymentRequestDto() {}

    // Constructor
    public ProcessPaymentRequestDto(String paymentIntentId, String paymentMethod,
                                   String cardLastFour, String cardBrand) {
        this.paymentIntentId = paymentIntentId;
        this.paymentMethod = paymentMethod;
        this.cardLastFour = cardLastFour;
        this.cardBrand = cardBrand;
    }

    // Getters and Setters
    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }
}
