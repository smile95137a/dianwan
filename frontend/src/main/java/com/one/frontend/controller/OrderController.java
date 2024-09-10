package com.one.frontend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.one.frontend.config.security.CustomUserDetails;
import com.one.frontend.config.security.SecurityUtils;
import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.Order;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.OrderQueryReq;
import com.one.frontend.request.PayCartRes;
import com.one.frontend.service.CartItemService;
import com.one.frontend.service.CartService;
import com.one.frontend.service.OrderDetailService;
import com.one.frontend.service.OrderService;
import com.one.frontend.util.ResponseUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;
	private final UserRepository userRepository;
	private final CartItemService cartItemService;
	private final CartService cartService;

//	// 根据ID获取订单
//	@GetMapping("/{userUid}")
//	public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable String userUid) {
//		Order order = orderService.getOrderById(userUid);
//
//		if (order == null) {
//			ApiResponse<Order> response = ResponseUtils.failure(404, "沒有此ID", null);
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//		}
//
//		ApiResponse<Order> response = ResponseUtils.success(200, null, order);
//		return ResponseEntity.ok(response);
//	}
//	
//	
	@PostMapping("/queryOrder")
	public ResponseEntity<?> queryOrder(@RequestBody OrderQueryReq req) {
	    CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
	    if (userDetails == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

        var userId = userDetails.getId();
        var list = orderService.queryOrders(userId,req);
        var res = ResponseUtils.success(200, null, list);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/storeProduct/pay")
	public ResponseEntity<?> payCartItem(@RequestBody PayCartRes payCartRes) {
		CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
		var userId = userDetails.getId();
		Long cartId = cartService.getCartIdByUserId(userId);

		if (cartId == null) {
			var response = ResponseUtils.failure(999, "無法找到購物車，請稍後重試", false);
			return ResponseEntity.ok(response);
		}

		var cartItemList = cartItemService.findByCartIdAndCartItemList(cartId, payCartRes.getCartItemIds());
		var orderNumber = orderService.createOrder(payCartRes, cartItemList, userId);

		return ResponseEntity.ok(ResponseUtils.success(200, "支付成功，订单已创建", orderNumber));
	}

	@GetMapping("/storeProduct/{orderNumber}")
	public ResponseEntity<?> getStoreProductOrderById(@PathVariable String orderNumber) {
		CustomUserDetails userDetails = SecurityUtils.getCurrentUserPrinciple();
		var userId = userDetails.getId();
		var res = orderService.getOrderByOrderNumber(userId, orderNumber);
		return ResponseEntity.ok(ResponseUtils.success(200, null, res));
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
			updateCustomerAccount(merchantTradeNo, tradeAmt, memberId);

			// 返回 1|OK 告知绿界已成功接收
			response.setStatus(HttpServletResponse.SC_OK);
			return "1|OK";
		} else {
			// 处理支付失败的情况
			return handlePaymentFailure(paymentData);
		}
	}

	private void updateCustomerAccount(String merchantTradeNo, String tradeAmt, String memberId) {

		userRepository.updateAccoutnt(tradeAmt, memberId);
		// 实现更新账户余额逻辑
		System.out.println("Update account balance for trade number: " + merchantTradeNo + " with amount: " + tradeAmt);
	}

	private String handlePaymentFailure(Map<String, String> paymentData) {
		// 实现失败处理逻辑
		System.out.println("Payment failed with message: " + paymentData.get("RtnMsg"));
		return "0|Error";
	}
}