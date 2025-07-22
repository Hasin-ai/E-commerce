package com.ecommerce.core.usecase.user;

public class RegisterUserResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String message;
    private boolean success;

    public RegisterUserResponse() {}

    public RegisterUserResponse(Long userId, String firstName, String lastName, String email, String phone, String message, boolean success) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.message = message;
        this.success = success;
    }

    public static RegisterUserResponse success(Long userId, String firstName, String lastName, String email, String phone, String message) {
        return new RegisterUserResponse(userId, firstName, lastName, email, phone, message, true);
    }

    public static RegisterUserResponse failure(String message) {
        return new RegisterUserResponse(null, null, null, null, null, message, false);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
