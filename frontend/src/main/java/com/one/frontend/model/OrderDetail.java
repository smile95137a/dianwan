package com.one.frontend.model;

import com.one.frontend.eenum.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "訂單詳情模型")
@Table(name = "order_detail")
@Entity
public class OrderDetail{

    @Schema(description = "訂單詳細唯一識別碼", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "訂單 ID", example = "1")
    @Column(name = "order_id")
    private Integer orderId;

    @Schema(description = "產品 ID", example = "1")
    @Column(name = "product_id")
    private Integer productId;

    @Schema(description = "商店產品 ID", example = "1")
    @Column(name = "store_product_id")
    private Integer storeProductId;

    @Schema(description = "商店產品名稱", example = "Product A")
    @Column(name = "store_product_name", length = 100)
    private String storeProductName;

    @Column(name = "product_detail_id")
    private Integer productDetailId;

    @Schema(description = "產品詳細名稱", example = "Detailed Product A")
    @Column(name = "product_detail_name", length = 255)
     private String productDetailName;

    @Schema(description = "數量", example = "2")
    @Column(name = "quantity")
    private Integer quantity;

    @Schema(description = "單價", example = "99.99")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Schema(description = "結果狀態", example = "SHIPPED")
    @Column(name = "result_status", length = 50)
    @Enumerated(EnumType.STRING)
    private OrderStatus resultStatus;

    @Schema(description = "結果項目 ID", example = "1001")
    @Column(name = "result_item_id")
    private Integer resultItemId;

    @Schema(description = "獲得的獎勳點數", example = "10")
    @Column(name = "bonus_points_earned")
    private Integer bonusPointsEarned;
}
