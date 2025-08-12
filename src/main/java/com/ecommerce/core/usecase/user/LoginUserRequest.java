package com.ecommerce.core.usecase.user;

public class LoginUserRequest {

    private final String email;
    private final String password;

    public LoginUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}