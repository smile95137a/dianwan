package com.one.onekuji.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReq {

    private Integer productDetailId;
    private Integer productId;
    private String description;
    private String note;
    private String size;
    private Integer quantity;
    private Integer stockQuantity;
    private String productName;
    private String grade;
    private BigDecimal price;
    private BigDecimal sliverPrice;
    private String imageUrl;
}
