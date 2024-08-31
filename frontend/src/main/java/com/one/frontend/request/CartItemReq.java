package com.one.frontend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReq {

    private Long cartId;
    private Long cartItemId;
    private Long storeProductId;
    private String storeProductName;
    private Integer quantity;
}
