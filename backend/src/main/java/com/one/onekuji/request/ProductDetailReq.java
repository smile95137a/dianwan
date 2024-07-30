package com.one.onekuji.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDetailReq {

    private Long productId;
    private String description;
    private String size;
    private String material;
    private int quantity;
    private String productName;
    private String grade;
    private String image;
    private Integer productDetailId;
}
