package com.one.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrawRequest {
    private Long prizeId;
    private Long prizeDetailId;
    private Long blindBoxId;
    private Long gachaId;
    private BigDecimal amount;
    private Integer totalDrawCount; //總共抽獎次數
    private Integer remainingDrawCount; // 剩餘抽獎次數
}
