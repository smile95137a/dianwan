package com.one.onekuji.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShippingMethodRes {

    private Long shippingMethodId;

    private String name;

    private BigDecimal size;

    private BigDecimal shippingPrice;

}
