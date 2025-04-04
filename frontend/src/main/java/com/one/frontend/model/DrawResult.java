package com.one.frontend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Schema(description = "抽獎結果模型")
@Table(name = "draw_result")
public class DrawResult{

    @Schema(description = "抽獎結果唯一識別碼", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drawId;

    @Schema(description = "抽獎 ID", example = "1")
    @Column(name = "user_id")
    private Long userId;

    @Schema(description = "產品 ID", example = "1")
    @Column(name = "product_id")
    private Long productId;

    @Schema(description = "產品詳細 ID", example = "1")
    @Column(name = "product_detail_id")
    private Long productDetailId;

    @Schema(description = "抽獎時間", example = "2024-08-22T15:30:00")
    @Column(name = "draw_time")
    private LocalDateTime drawTime;

    @Schema(description = "抽獎金額", example = "100.00")
    @Column(name = "amount")
    private BigDecimal amount;

    @Schema(description = "抽獎次數", example = "5")
    @Column(name = "draw_count")
    private Integer drawCount;

    @Schema(description = "剩餘抽獎次數", example = "2")
    @Column(name = "remaining_draw_count")
    private Integer remainingDrawCount;

    @Schema(description = "獎品編號", example = "PRIZE001")
    @Column(name = "prize_number", length = 50)
    private String prizeNumber;

    @Schema(description = "狀態", example = "ACTIVE")
    @Column(name = "status", length = 20)
    private String status;

    @Schema(description = "創建日期", example = "2024-08-22T15:30:00")
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Schema(description = "更新日期", example = "2024-08-22T15:30:00")
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "total_draw_count")
    private Long totalDrawCount;

    private Long remainingTime;

    @Schema(description = "圖片 URL 列表", example = "[\"https://example.com/image1.png\", \"https://example.com/image2.png\"]")
    @Column(name = "image_urls", length = 2000)  // 增加长度以确保可以存储多个 URL
    private String imageUrls; // 将 List<String> 存为单个字符串

    // Getter 和 Setter 方法，进行序列化和反序列化
    public List<String> getImageUrls() {
        if (this.imageUrls == null || this.imageUrls.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.imageUrls.split(",")); // 假设使用逗号分隔符
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = String.join(",", imageUrls); // 使用逗号分隔符将列表转换为字符串
    }

    private String productName;

    @Column(name = "sliver_price")
    private BigDecimal sliverPrice;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "bonus_price")
    private BigDecimal bonusPrice;

    @Column(name = "pay_type")
    private String payType;
}
