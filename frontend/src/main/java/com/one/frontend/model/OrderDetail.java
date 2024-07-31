package com.one.frontend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "訂單詳情實體類")
public class OrderDetail {

    @Schema(description = "訂單詳情ID")
    private Long id;

    @Schema(description = "訂單ID")
    private Long orderId;

    @Schema(description = "獎品ID")
    private Long productId;

    @Schema(description = "獎品詳情ID")
    private Long productDetailId;

    @Schema(description = "獎品詳情名稱")
    private String productDetailName;

    @Schema(description = "數量")
    private Integer quantity;

    @Schema(description = "單價")
    private BigDecimal unitPrice;

    @Schema(description = "總價")
    private BigDecimal totalPrice;

    @Schema(description = "結果狀態（'PENDING', 'COMPLETED', 'FAILED'）")
    private String resultStatus;

    @Schema(description = "結果項目ID（關聯到 BlindBoxResult, GachaResult, 或 PrizeResult）")
    private Long resultItemId;

    @Schema(description = "獲得的積分")
    private BigDecimal bonusPointsEarned;
}
