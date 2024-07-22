package com.one.onekuji.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BlindBox {
    private Long blindBoxId;
    private String blindBoxName;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private int soldQuantity;
    private String imageUrl;
    private List<String> itemList;
    private LocalDateTime releaseDate;
    private boolean isLimited;
    private String manufacturer;
    private String category;
    private int rarity;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
