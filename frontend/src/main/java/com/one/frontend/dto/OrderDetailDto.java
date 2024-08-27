package com.one.frontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDto {

    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productDetailName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer resultItemId;
}
