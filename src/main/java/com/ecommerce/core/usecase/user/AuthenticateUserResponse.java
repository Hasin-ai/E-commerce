package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;

public class AuthenticateUserResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private boolean authenticated;
    private String message;

    public AuthenticateUserResponse() {}

    public AuthenticateUserResponse(User user, String token, boolean authenticated, String message) {
        if (user != null) {
            this.userId = user.getId();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.email = user.getEmail().getValue();
        }
        this.token = token;
        this.authenticated = authenticated;
        this.message = message;
    }

    public static AuthenticateUserResponse success(User user, String token, String message) {
        return new AuthenticateUserResponse(user, token, true, message);
    }

    public static AuthenticateUserResponse failure(String message) {
        return new AuthenticateUserResponse(null, null, false, message);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
