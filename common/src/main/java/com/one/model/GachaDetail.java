package com.one.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "GachaDetail 模型，表示 Gacha 的详细信息")
public class GachaDetail {

    @Schema(description = "GachaDetail 的 ID", example = "1")
    private Long gachaDetailId;

    @Schema(description = "Gacha 的 ID", example = "1")
    private Long gachaId;

    private String gachaName;

    @Schema(description = "Gacha 详细信息的描述", example = "描述 Gacha 中的一个特定细节")
    private String description;

    @Schema(description = "Gacha 详细信息的稀有度", example = "5")
    private int rarity;

    @Schema(description = "Gacha 详细信息的数量", example = "10")
    private int quantity;

    @Schema(description = "Gacha 详细信息的图片 URL", example = "http://example.com/gacha-detail.jpg")
    private String imageUrl;

    @Schema(description = "Gacha 详细信息的创建日期", example = "2023-07-01T12:34:56")
    private LocalDateTime createDate;

    @Schema(description = "Gacha 详细信息的更新日期", example = "2023-07-20T08:45:23")
    private LocalDateTime updateDate;

    @Schema(description = "Gacha 详细信息的状态", example = "有效")
    private String status;

    @Schema(description = "Gacha 详细信息的等级", example = "A")
    private String grade;
}
