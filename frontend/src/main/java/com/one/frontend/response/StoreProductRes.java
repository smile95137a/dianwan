package com.one.frontend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductRes {
    private Long storeProductId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private String categoryId;
    private String status;
    private BigDecimal specialPrice;
    private String shippingMethod;
    private BigDecimal size;
    private BigDecimal shippingPrice;
}
