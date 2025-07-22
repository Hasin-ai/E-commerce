package com.ecommerce.adapter.web.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequestDto {

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    // Default constructor
    public ForgotPasswordRequestDto() {}

    // Constructor
    public ForgotPasswordRequestDto(String email) {
        this.email = email;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
