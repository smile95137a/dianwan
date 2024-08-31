package com.one.frontend.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRes {
    private Long cartItemId;          // CartItem的唯一标识符
    private Long cartId;              // Cart的唯一标识符
    private Integer storeProductId;
    private String storeProductName;  // StoreProduct的名称
    private Integer quantity;         // 商品数量
    private BigDecimal unitPrice;     // 商品单价
    private BigDecimal totalPrice;    // 总价
    private boolean isPay;            // 是否要支付
    private String imageUrl;          // 商品图片URL
    private String productName;       // 商品名称
    private BigDecimal specialPrice;  // 特价
    private BigDecimal size;          // 商品尺寸
}
