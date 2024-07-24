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
    private Order order;
    private Long productId;
    private String productName;
    private String productType; // "BLIND_BOX", "GACHA", "PRIZE"
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String resultStatus; // "PENDING", "COMPLETED", "FAILED"
    private Long resultItemId; // 關聯到 BlindBoxResult, GachaResult, 或 PrizeResult
    private BigDecimal bonusPointsEarned;
}