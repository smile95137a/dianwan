package com.one.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrizeNumber {
    private Long prizeNumberId;
    private Long productDetailId;
    private Long productId;
    private Integer number; // 奖品的唯一编号
    private Boolean isDrawn; // 该编号是否已被抽走

    private String grade; // 可能为 null

}