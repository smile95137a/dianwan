package com.one.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Blind Box 模型，表示具有各種屬性的收藏品")
public class BlindBox {

    @Schema(description = "Blind Box 的 ID", example = "1")
    private Long blindBoxId;

    @Schema(description = "Blind Box 的名稱", example = "超級稀有箱")
    private String blindBoxName;

    @Schema(description = "Blind Box 的描述", example = "包含稀有收藏品的盲盒")
    private String description;

    @Schema(description = "Blind Box 的價格", example = "19.99")
    private BigDecimal price;

    @Schema(description = "Blind Box 的庫存數量", example = "100")
    private int stockQuantity;

    @Schema(description = "已售出的 Blind Box 數量", example = "50")
    private int soldQuantity;

    @Schema(description = "表示 Blind Box 的圖片 URL", example = "http://example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "Blind Box 中包含的項目列表")
    private List<String> itemList;

    @Schema(description = "Blind Box 的發佈日期", example = "2023-08-15T10:00:00")
    private LocalDateTime releaseDate;

    @Schema(description = "是否為限量版 Blind Box", example = "true")
    private boolean isLimited;

    @Schema(description = "Blind Box 的製造商", example = "頂級製造商有限公司")
    private String manufacturer;

    @Schema(description = "Blind Box 的類別", example = "玩具")
    private String category;

    @Schema(description = "Blind Box 的稀有度等級", example = "5")
    private int rarity;

    @Schema(description = "Blind Box 是否啟用", example = "true")
    private boolean isActive;

    @Schema(description = "Blind Box 記錄的創建時間戳", example = "2023-07-01T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "Blind Box 記錄的最後更新時間戳", example = "2023-07-20T08:45:23")
    private LocalDateTime updatedAt;

    @Schema(description = "Blind Box 的可用開始日期", example = "2023-09-01T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "Blind Box 的可用結束日期", example = "2023-12-31T23:59:59")
    private LocalDateTime endDate;

    @Schema(description = "新增人員" , example = "aaaa")
    private String createdUser;

    @Schema(description = "更新人員" , example = "xxxx")
    private String updateUser;

    @Schema(description = "未出貨數量" , example = "0")
    private Integer unshippedQuantity;
}
