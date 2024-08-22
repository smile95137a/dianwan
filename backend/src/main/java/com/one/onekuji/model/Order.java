package com.one.onekuji.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Schema(description = "訂單模型")
@Table(name = "order")
public class Order{

    @Schema(description = "訂單唯一識別碼", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "訂單編號", example = "ORD123456")
    @Column(name = "order_number", length = 50)
    private String orderNumber;

    @Schema(description = "用戶 ID", example = "1")
    @Column(name = "user_id")
    private Integer userId;

    @Schema(description = "總金額", example = "199.98")
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Schema(description = "獲得的獎勳點數", example = "10")
    @Column(name = "bonus_points_earned")
    private Integer bonusPointsEarned;

    @Schema(description = "使用的獎勳點數", example = "5")
    @Column(name = "bonus_points_used")
    private Integer bonusPointsUsed;

    @Schema(description = "狀態", example = "PENDING")
    @Column(name = "status", length = 50)
    private String status;

    @Schema(description = "支付方式", example = "CREDIT_CARD")
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Schema(description = "支付狀態", example = "PAID")
    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Schema(description = "創建日期", example = "2024-08-22T15:30:00")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新日期", example = "2024-08-22T15:30:00")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Schema(description = "支付日期", example = "2024-08-22T16:00:00")
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Schema(description = "備註", example = "Please deliver by end of the week.")
    @Column(name = "notes")
    private String notes;
}
