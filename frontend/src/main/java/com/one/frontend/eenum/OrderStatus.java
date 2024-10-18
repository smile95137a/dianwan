package com.one.frontend.eenum;

public enum OrderStatus {
    PREPARING_SHIPMENT("準備發貨"),
    SHIPPED("已發貨"),

    SOLD_OUT("售罄");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
