package com.stockflow.backend.dto;



import com.stockflow.backend.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;
    private String name;
    private String sku;
    private String description;
    private Integer quantityOnHand;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private Integer lowStockThreshold;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.sku = product.getSku();
        this.description = product.getDescription();
        this.quantityOnHand = product.getQuantityOnHand();
        this.costPrice = product.getCostPrice();
        this.sellingPrice = product.getSellingPrice();
        this.lowStockThreshold = product.getLowStockThreshold();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public String getDescription() { return description; }
    public Integer getQuantityOnHand() { return quantityOnHand; }
    public BigDecimal getCostPrice() { return costPrice; }
    public BigDecimal getSellingPrice() { return sellingPrice; }
    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

