package com.one.onekuji.eenum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PrizeCategory {

    FIGURE("一番賞") ,
    C3("家電一番賞") ,
    BONUS("紅利一番賞"),
    NONE("無");

    private final String description;

    PrizeCategory(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PrizeCategory fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return NONE; // 返回默认值
        }

        for (PrizeCategory category : PrizeCategory.values()) {
            if (category.description.equals(value) || category.name().equals(value)) {
                return category;
            }
        }

        throw new IllegalArgumentException("未知的 PrizeCategory 值: " + value);
    }
}
