package com.one.frontend.dto;

import com.one.frontend.eenum.PrizeCategory;
import com.one.frontend.eenum.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DrawRequest {
    private Long productId;
    private Long productDetailId;
    private ProductType productType;
    private PrizeCategory prizeCategory;
    private BigDecimal amount;
    private Integer totalDrawCount; //總共抽獎次數
    private Integer remainingDrawCount; // 剩餘抽獎次數
}
