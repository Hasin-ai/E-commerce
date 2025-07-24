package com.ecommerce.adapter.web.dto.response;

public class AuthResponseDto {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private UserResponseDto user;

    public AuthResponseDto() {}

    public AuthResponseDto(String accessToken, String tokenType, long expiresIn, UserResponseDto user) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserResponseDto getUser() {
        return user;
    }

    public void setUser(UserResponseDto user) {
        this.user = user;
    }
}