package com.one.frontend.response;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRes {

    private Long orderId;
    private String orderNumber;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer bonusPointsEarned;
    private Integer bonusPointsUsed;
    private String resultStatus;
    private List<OrderDetailRes> orderDetails;
    private LocalDateTime createdAt;


}
