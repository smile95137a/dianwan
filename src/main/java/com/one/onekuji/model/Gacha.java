package com.one.onekuji.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gacha {
    private Long gachaId;
    private String gachaName;
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
