package com.one.onekuji.request;

import com.one.onekuji.eenum.StoreProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductReq {
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private String categoryId;
    private StoreProductStatus status;
    private BigDecimal specialPrice;
    private String shippingMethod;
    private BigDecimal size;
    private BigDecimal shippingPrice;
}
