package com.stockflow.backend.dto;


import java.util.List;

public class DashboardResponse {

    private long totalProducts;
    private long totalQuantity;
    private long lowStockCount;
    private List<ProductResponse> lowStockProducts;

    public DashboardResponse(
            long totalProducts,
            long totalQuantity,
            List<ProductResponse> lowStockProducts) {
        this.totalProducts = totalProducts;
        this.totalQuantity = totalQuantity;
        this.lowStockProducts = lowStockProducts;
        this.lowStockCount = lowStockProducts.size();
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public List<ProductResponse> getLowStockProducts() {
        return lowStockProducts;
    }
}