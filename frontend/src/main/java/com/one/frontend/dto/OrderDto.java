package com.one.frontend.dto;

import com.one.frontend.eenum.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {

    private Integer id;

    private String orderNumber;

    private Integer userId;

    private BigDecimal totalAmount;

    private Integer bonusPointsEarned;

    private Integer bonusPointsUsed;

    private LocalDateTime createdAt;
    private OrderStatus resultStatus;
}
