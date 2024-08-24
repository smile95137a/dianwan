package com.one.frontend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRes {
    private Integer productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private BigDecimal sliverPrice;
    private Integer stockQuantity;
    private String imageUrl;
    private String productType;
    private String prizeCategory;
    private String status;
}
