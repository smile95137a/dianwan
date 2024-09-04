package com.one.frontend.model;

import java.math.BigDecimal;

import com.one.frontend.eenum.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "訂單詳情模型")
@Table(name = "order_detail")
@Entity
public class OrderDetail {

	@Schema(description = "訂單詳細唯一識別碼", example = "1")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Schema(description = "訂單 ID", example = "1")
	@Column(name = "order_id")
	private Long orderId;

	@Schema(description = "產品 ID", example = "1")
	@Column(name = "product_id")
	private Long productId;

	@Schema(description = "商店產品 ID", example = "1")
	@Column(name = "store_product_id")
	private Long storeProductId;

	@Schema(description = "數量", example = "2")
	@Column(name = "quantity")
	private Integer quantity;

	@Schema(description = "單價", example = "99.99")
	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	@Schema(description = "結果項目 ID", example = "1001")
	@Column(name = "result_item_id")
	private Integer resultItemId;

	@Schema(description = "獲得的獎勳點數", example = "10")
	@Column(name = "bonus_points_earned")
	private Integer bonusPointsEarned;

	@Column(name = "total_price")
	private BigDecimal totalPrice;
}
