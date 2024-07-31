package com.one.onekuji.model;

import com.one.onekuji.eenum.PrizeCategory;
import com.one.onekuji.eenum.ProductStatus;
import com.one.onekuji.eenum.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product 模型，表示商品的基本信息")
public class Product {

    @Schema(description = "商品的 ID", example = "1")
    private Long productId;

    @Schema(description = "商品的名稱", example = "驚喜盲盒")
    private String productName;

    @Schema(description = "商品的描述", example = "這是一個驚喜盲盒，裡面有隨機的獎品")
    private String description;

    @Schema(description = "商品的價格", example = "29.99")
    private Double price;

    @Schema(description = "商品的庫存數量", example = "100")
    private Integer stockQuantity;

    @Schema(description = "商品的售出數量", example = "10")
    private Integer soldQuantity;

    @Schema(description = "商品的圖片 URL", example = "http://example.com/product.jpg")
    private String imageUrl;

    @Schema(description = "商品的稀有度", example = "5")
    private Integer rarity;

    @Schema(description = "商品的創建日期", example = "2023-07-01T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "商品的開始日期", example = "2023-07-01T12:34:56")
    private LocalDateTime startDate;

    @Schema(description = "商品的結束日期", example = "2023-08-01T12:34:56")
    private LocalDateTime endDate;

    @Schema(description = "創建商品的用戶", example = "testUser")
    private String createdUser;

    @Schema(description = "更新商品的用戶", example = "updatedUser")
    private String updateUser;

    private LocalDateTime updatedAt;

    private ProductType productType;

    @Schema(description = "一番賞的具體類別", example = "BONUS")
    private PrizeCategory prizeCategory;

    private ProductStatus status;
}
