package com.shop.ecommerce.models.enums;

public enum  ShopStatus {
    ACTIVE("active"),
    INACTIVE("inactive");
    private final String status;

    ShopStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
