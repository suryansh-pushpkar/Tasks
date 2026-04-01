package com.friendbook.dto;

public class LoginResponse {

    private final String message;
    private final String redirectUrl;
    private final String token;

    public LoginResponse(String message, String redirectUrl, String token) {
        this.message = message;
        this.redirectUrl = redirectUrl;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getToken() {
        return token;
    }
}
