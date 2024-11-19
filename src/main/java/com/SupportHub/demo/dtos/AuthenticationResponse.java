package com.SupportHub.demo.dtos;

public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse() {}

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    // Getter and Setter

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
