package com.one.onekuji.request;

import com.one.eenum.PrizeCategory;
import com.one.eenum.ProductStatus;
import com.one.eenum.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductReq {

    private Long productId;
    private Long userId;
    private String productName;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Integer soldQuantity;
    private String imageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ProductType productType;
    private PrizeCategory prizeCategory;
    private ProductStatus status;
}
