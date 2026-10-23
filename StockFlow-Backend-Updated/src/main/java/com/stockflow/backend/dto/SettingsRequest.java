package com.stockflow.backend.dto;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SettingsRequest {

    @NotNull(message = "Default low-stock threshold is required")
    @Min(value = 0, message = "Threshold cannot be negative")
    private Integer defaultLowStockThreshold;

    public Integer getDefaultLowStockThreshold() {
        return defaultLowStockThreshold;
    }

    public void setDefaultLowStockThreshold(
            Integer defaultLowStockThreshold) {
        this.defaultLowStockThreshold = defaultLowStockThreshold;
    }
}
