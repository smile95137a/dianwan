package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.Order;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.service.OrderService;
import com.one.frontend.util.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	private UserRepository userRepository;

	// 根据ID获取订单
	@GetMapping("/order/{userId}")
	public ResponseEntity<ApiResponse<List<Order>>> getOrderById(@PathVariable Long userId) {
		List<Order> order = orderService.getOrderById(userId);

		if (order == null || order.isEmpty()) {
			ApiResponse<List<Order>> response = ResponseUtils.failure(404, "沒有此ID", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		ApiResponse<List<Order>> response = ResponseUtils.success(200, null, order);
		return ResponseEntity.ok(response);
	}



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