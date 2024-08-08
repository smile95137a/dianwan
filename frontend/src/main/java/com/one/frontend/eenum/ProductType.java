package com.one.frontend.eenum;

public enum ProductType {
    PRIZE("一番賞"),
    GACHA("扭蛋"),
    BLIND_BOX("盲盒");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

