package com.stockflow.backend.dto;


public class SettingsResponse {

    private String organizationName;
    private Integer defaultLowStockThreshold;

    public SettingsResponse(
            String organizationName,
            Integer defaultLowStockThreshold) {
        this.organizationName = organizationName;
        this.defaultLowStockThreshold = defaultLowStockThreshold;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public Integer getDefaultLowStockThreshold() {
        return defaultLowStockThreshold;
    }
}
