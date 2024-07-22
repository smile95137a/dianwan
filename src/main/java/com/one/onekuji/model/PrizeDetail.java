package com.one.onekuji.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrizeDetail {
    private Long prizeId;
    private String description;
    private int rarity;
    private String size;
    private String material;
    private boolean isSecret;
    private int quantity;
    private Integer PrizeDetailId;
    private String productName;
    private String grade;
    private Integer count;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    //產品URL
    private String image;
    private String status;
}