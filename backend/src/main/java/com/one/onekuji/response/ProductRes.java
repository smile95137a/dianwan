package com.one.onekuji.response;

import com.one.onekuji.eenum.PrizeCategory;
import com.one.onekuji.eenum.ProductStatus;
import com.one.onekuji.eenum.ProductType;
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
    private ProductType productType;
    private PrizeCategory prizeCategory;
    private ProductStatus status;
    private BigDecimal bonusPrice;
}
