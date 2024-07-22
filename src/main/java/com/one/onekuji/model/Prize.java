package com.one.onekuji.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "獎品模型，包含各種屬性")
public class Prize {

    @Schema(description = "獎品價格", example = "29.99")
    private BigDecimal price;

    @Schema(description = "獎品總數量", example = "1000")
    private int totalQuantity;

    @Schema(description = "獎品剩餘數量", example = "500")
    private int remainingQuantity;

    @Schema(description = "獎品主圖的 URL", example = "http://example.com/image.jpg")
    private String mainImageUrl;

    @Schema(description = "獎品發布日期", example = "2024-01-01")
    private LocalDate releaseDate;

    @Schema(description = "製造商", example = "頂級製造商有限公司")
    private String manufacturer;

    @Schema(description = "類別", example = "電子產品")
    private String category;

    @Schema(description = "是否為限量版", example = "true")
    private boolean isLimited;

    @Schema(description = "是否啟用", example = "true")
    private boolean isActive;

    @Schema(description = "獎品唯一標識符", example = "123")
    private Integer prizeId;

    @Schema(description = "獎品名稱", example = "超級小工具")
    private String name;

    @Schema(description = "獎品詳情列表")
    private List<PrizeDetail> prizeDetails;

    @Schema(description = "創建時間戳", example = "2023-07-01T12:34:56")
    private LocalDateTime createDate;

    @Schema(description = "最後更新時間戳", example = "2023-07-20T08:45:23")
    private LocalDateTime updateDate;

    @Schema(description = "獎品可用的開始日期", example = "2023-09-01T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "獎品可用的結束日期", example = "2024-01-01T23:59:59")
    private LocalDateTime endDate;

    @Schema(description = "狀態", example = "可用")
    private String status;

    @Schema(description = "描述", example = "這是一個很棒的獎品")
    private String description;

    @Schema(description = "新增人員" , example = "aaaa")
    private String createdUser;

    @Schema(description = "更新人員" , example = "xxxx")
    private String updateUser;
}
