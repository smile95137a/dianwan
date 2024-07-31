package com.one.onekuji.eenum;

public enum PrizeCategory {

    FIGURE("一番賞") ,
    C_FIGURE("家電一番賞") ,
    BONUS_FIGURE("紅利一番賞");

    private final String description;

    PrizeCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PrizeCategory fromDescription(String description) {
        for (PrizeCategory category : PrizeCategory.values()) {
            if (category.getDescription().equals(description)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown description: " + description);
    }

}
