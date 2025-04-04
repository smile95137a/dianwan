package com.one.frontend.dto;

import com.one.frontend.eenum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDto {

    private Long id;
    private Long orderId;
    private Long productId;
    private Long productDetailId;
    private String storeProductName;
    private String productDetailName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer resultItemId;
    private OrderStatus resultStatus;
}
