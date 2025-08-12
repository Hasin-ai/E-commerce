package com.ecommerce.adapter.web.dto.response.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ApiResponseDto<T> {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("errors")
    private java.util.List<String> errors;

    @JsonProperty("pagination")
    private PaginationDto pagination;

    // Default constructor
    public ApiResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for success response
    public ApiResponseDto(T data, String message) {
        this();
        this.success = true;
        this.data = data;
        this.message = message;
    }

    // Constructor for error response
    public ApiResponseDto(String message, java.util.List<String> errors) {
        this();
        this.success = false;
        this.message = message;
        this.errors = errors;
    }

    // Static factory methods
    public static <T> ApiResponseDto<T> success(T data) {
        return new ApiResponseDto<>(data, "Operation completed successfully");
    }

    public static <T> ApiResponseDto<T> success(T data, String message) {
        return new ApiResponseDto<>(data, message);
    }

    public static <T> ApiResponseDto<T> error(String message) {
        ApiResponseDto<T> response = new ApiResponseDto<>();
        response.success = false;
        response.message = message;
        response.errors = null;
        return response;
    }

    public static <T> ApiResponseDto<T> error(String message, java.util.List<String> errors) {
        return new ApiResponseDto<>(message, errors);
    }

    // Getters and Setters
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public java.util.List<String> getErrors() {
        return errors;
    }

    public void setErrors(java.util.List<String> errors) {
        this.errors = errors;
    }

    public PaginationDto getPagination() {
        return pagination;
    }

    public void setPagination(PaginationDto pagination) {
        this.pagination = pagination;
    }
}
