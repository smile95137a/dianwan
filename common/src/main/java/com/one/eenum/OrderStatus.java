package com.one.eenum;

public enum OrderStatus {
    PREPARING_SHIPMENT("準備發貨"),
    SHIPPED("已發貨");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
