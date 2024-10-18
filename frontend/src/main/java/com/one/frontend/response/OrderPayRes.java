package com.one.frontend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayRes {

    private String orderNumber;

    private PaymentResponse paymentRequest;
}
