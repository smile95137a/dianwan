package com.one.onekuji.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.one.onekuji.eenum.PrizeCategory;
import com.one.onekuji.eenum.ProductStatus;
import com.one.onekuji.eenum.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductReq {

    private Long productId;
    private Long userId;
    private String productName;
    private String description;
    private Long price;
    private Integer stockQuantity;
    private Integer soldQuantity;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endDate;
    private ProductType productType;
        private PrizeCategory prizeCategory;
    private ProductStatus status;
}
