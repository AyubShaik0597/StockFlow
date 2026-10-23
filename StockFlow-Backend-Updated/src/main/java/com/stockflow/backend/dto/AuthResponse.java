package com.stockflow.backend.dto;


public class AuthResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private Long organizationId;
    private String email;
    private String organizationName;

    public AuthResponse() {
    }

    public AuthResponse(
            String token,
            Long userId,
            Long organizationId,
            String email,
            String organizationName) {

        this.token = token;
        this.tokenType = "Bearer";
        this.userId = userId;
        this.organizationId = organizationId;
        this.email = email;
        this.organizationName = organizationName;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getEmail() {
        return email;
    }

    public String getOrganizationName() {
        return organizationName;
    }
}

