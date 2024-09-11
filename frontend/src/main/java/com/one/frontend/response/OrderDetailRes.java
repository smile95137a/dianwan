package com.one.frontend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetailRes {



	private Long orderDetailId;
	private Long productId;
	private String productDetailName;
	private Integer quantity;
	private BigDecimal unitPrice;
	private Integer totalPrice;
	private StoreProductRes storeProduct;


}
