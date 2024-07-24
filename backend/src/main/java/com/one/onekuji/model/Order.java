package com.one.onekuji.model;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String orderNumber;
    private User user;
    private BigDecimal totalAmount;
    private BigDecimal bonusPointsEarned;
    private BigDecimal bonusPointsUsed;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
    private Set<OrderDetail> orderDetails;
    private String notes;
}