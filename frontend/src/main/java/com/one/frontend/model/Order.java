package com.one.frontend.model;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "訂單實體類")
public class Order {

    @Schema(description = "訂單ID")
    private Long id;

    @Schema(description = "訂單號")
    private String orderNumber;

    @Schema(description = "用戶信息")
    private User user;

    @Schema(description = "用戶ID")
    private Long userId;

    @Schema(description = "總金額")
    private BigDecimal totalAmount;

    @Schema(description = "獲得的積分")
    private BigDecimal bonusPointsEarned;

    @Schema(description = "使用的積分")
    private BigDecimal bonusPointsUsed;

    @Schema(description = "訂單狀態")
    private String status;

    @Schema(description = "支付方式")
    private String paymentMethod;

    @Schema(description = "支付狀態")
    private String paymentStatus;

    @Schema(description = "創建時間")
    private LocalDateTime createdAt;

    @Schema(description = "更新時間")
    private LocalDateTime updatedAt;

    @Schema(description = "支付時間")
    private LocalDateTime paidAt;

    @ArraySchema(schema = @Schema(description = "訂單詳情"))
    private Set<OrderDetail> orderDetails;

    @Schema(description = "備註")
    private String notes;
}
