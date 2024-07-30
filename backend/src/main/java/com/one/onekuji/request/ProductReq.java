package com.one.onekuji.request;

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
}
