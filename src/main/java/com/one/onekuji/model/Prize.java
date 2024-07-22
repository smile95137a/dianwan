package com.one.onekuji.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prize {
    private BigDecimal price;
    private int totalQuantity;
    private int remainingQuantity;
    private String mainImageUrl;
    private LocalDate releaseDate;
    private String manufacturer;
    private String category;
    private boolean isLimited;
    private boolean isActive;
    private Integer prizeId; // 奖品唯一标识符
    private String name; // 奖品名称
    private List<PrizeDetail> prizeDetails; // 奖品明细列表
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String description;
}