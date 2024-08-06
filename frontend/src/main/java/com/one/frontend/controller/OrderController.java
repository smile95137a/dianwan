package com.one.frontend.controller;

import com.one.frontend.repository.UserRepository;
import com.one.frontend.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout(@RequestBody Integer userId) {

        return orderService.ecpayCheckout(userId);
	}


	@PostMapping(value = "/returnUrl", consumes = "application/x-www-form-urlencoded")
	public String handleEcpayNotification(@RequestParam Map<String, String> paymentData, HttpServletResponse response) {
		// 打印接收到的数据，实际部署时应该移除或使用日志记录
		System.out.println("Received payment data: " + paymentData);

		// 检查返回的支付状态码
		if ("1".equals(paymentData.get("RtnCode"))) {
			// 付款成功
			String merchantTradeNo = paymentData.get("MerchantTradeNo");
			String tradeAmt = paymentData.get("TradeAmt");
			String memberId = paymentData.get("CustomField1");
			System.out.println(merchantTradeNo);
			System.out.println(tradeAmt);
			// TODO: 在这里更新用户的账户余额和订单状态
			updateCustomerAccount(merchantTradeNo, tradeAmt , memberId);

			// 返回 1|OK 告知绿界已成功接收
			response.setStatus(HttpServletResponse.SC_OK);
			return "1|OK";
		} else {
			// 处理支付失败的情况
			return handlePaymentFailure(paymentData);
		}
	}

	private void updateCustomerAccount(String merchantTradeNo, String tradeAmt , String memberId) {

		userRepository.updateAccoutnt(tradeAmt , memberId);
		// 实现更新账户余额逻辑
		System.out.println("Update account balance for trade number: " + merchantTradeNo + " with amount: " + tradeAmt);
	}

	private String handlePaymentFailure(Map<String, String> paymentData) {
		// 实现失败处理逻辑
		System.out.println("Payment failed with message: " + paymentData.get("RtnMsg"));
		return "0|Error";
	}
}