package com.stockflow.backend.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 80)
    private String sku;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Quantity on hand is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantityOnHand = 0;

    @DecimalMin(value = "0.0", message = "Cost price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Cost price can have up to 10 integer digits and 2 decimal places")
    private BigDecimal costPrice;

    @DecimalMin(value = "0.0", message = "Selling price cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Selling price can have up to 10 integer digits and 2 decimal places")
    private BigDecimal sellingPrice;

    @Min(value = 0, message = "Threshold cannot be negative")
    private Integer lowStockThreshold;

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
}

