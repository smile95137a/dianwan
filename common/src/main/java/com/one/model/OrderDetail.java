package com.one.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private Long id;
    private Long orderId;
    private Long prizeId;
    private Long prizeDetailId;
    private String prizeDetailName;
    private Long blindBoxId;
    private Long blindBoxDetailId;
    private String blindBoxName;
    private Long gachaId;
    private Long gachaDetailId;
    private String gachaName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String resultStatus; // "PENDING", "COMPLETED", "FAILED"
    private Long resultItemId; // 關聯到 BlindBoxResult, GachaResult, 或 PrizeResult
    private BigDecimal bonusPointsEarned;
}