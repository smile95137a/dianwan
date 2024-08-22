package com.one.onekuji.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Schema(description = "運輸方式模型")
@Table(name = "shipping_method")
@Entity
public class ShippingMethod{

    @Schema(description = "運輸方式唯一識別碼", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "名稱", example = "Standard Shipping")
    @Column(name = "name", length = 255)
    private String name;

    @Schema(description = "描述", example = "Standard shipping with tracking.")
    @Column(name = "description")
    private String description;

    @Schema(description = "物品大小")
    @Column(name = "size")
    private Integer size;

    @Schema(description = "運費", example = "1")
    @Column(name = "shipping_price")
    private Integer shippingPrice;

    @Schema(description = "創建日期", example = "2024-08-22T15:30:00")
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Schema(description = "更新日期", example = "2024-08-22T15:30:00")
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
