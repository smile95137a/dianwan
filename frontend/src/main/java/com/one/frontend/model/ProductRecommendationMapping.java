package com.one.frontend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "商品與推薦類別關聯模型")
@Table(name = "product_recommendation_mapping")
@Entity
public class ProductRecommendationMapping {

    @Schema(description = "關聯唯一識別碼", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Schema(description = "商店產品 ID", example = "1")
    @ManyToOne
    @JoinColumn(name = "store_product_id", referencedColumnName = "store_product_id")
    private StoreProduct storeProduct;

    @Schema(description = "推薦類別 ID", example = "1")
    @ManyToOne
    @JoinColumn(name = "recommendation_id", referencedColumnName = "id")
    private StoreProductRecommendation storeProductRecommendation;

    @Schema(description = "創建時間", example = "2024-08-22T15:30:00")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新時間", example = "2024-08-22T15:30:00")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Schema(description = "創建用戶 ID", example = "1")
    @Column(name = "created_user")
    private Long createdUser;

    @Schema(description = "更新用戶 ID", example = "1")
    @Column(name = "update_user")
    private Long updateUser;
}
