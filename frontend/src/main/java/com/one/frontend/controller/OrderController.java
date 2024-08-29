package com.one.frontend.controller;

import com.one.frontend.eenum.OrderStatus;
import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.CartItem;
import com.one.frontend.model.Order;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.StoreOrderDetailReq;
import com.one.frontend.service.CartItemService;
import com.one.frontend.service.CartService;
import com.one.frontend.service.OrderDetailService;
import com.one.frontend.service.OrderService;
import com.one.frontend.util.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	private OrderDetailService orderDetailService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartService cartService;

	// 根据ID获取订单
	@GetMapping("/order/{userUid}")
	public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable String userUid) {
		Order order = orderService.getOrderById(userUid);

		if (order == null) {
			ApiResponse<Order> response = ResponseUtils.failure(404, "沒有此ID", null);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

		ApiResponse<Order> response = ResponseUtils.success(200, null, order);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/storeProduct/pay/{userUid}")
	public ResponseEntity<ApiResponse<List<Order>>> payCartItem(@PathVariable String userUid) {
		// 1. 获取用户的所有购物车项
		List<CartItem> cartList = cartService.findByUserUidAndIsPayFalse(userUid);

		if (cartList.isEmpty()) {

		}

		// 2. 计算总金额
		BigDecimal totalAmount = cartList.stream()
				.map(item -> item.getTotalPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		// 3. 使用万事达卡进行支付（假设 paymentService.processPayment 是支付服务的调用方法）
		boolean paymentSuccess = true;

		if (!paymentSuccess) {

		}
		String orderNumber = UUID.randomUUID().toString();
		// 4. 创建订单并保存订单详情
		Order order = new Order();
		order.setUserUid(userUid);
		order.setOrderNumber(orderNumber);
		order.setTotalAmount(totalAmount);
		order.setCreatedAt(LocalDateTime.now());
		order.setResultStatus(OrderStatus.PREPARING_SHIPMENT);
		order.setPaidAt(LocalDateTime.now());
		orderService.save(order);
		Order orderReq = orderService.getOrderById(orderNumber);
		for(CartItem cartItem : cartList){
			StoreOrderDetailReq orderDetail = new StoreOrderDetailReq();
			orderDetail.setOrderId(orderReq.getId());
			orderDetail.setStoreProductId(Math.toIntExact(cartItem.getStoreProductId()));
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setUnitPrice(cartItem.getUnitPrice());
			orderDetailService.save(orderDetail);
		}


		// 5. 更新 CartItem 的 isPay 字段为 true
		cartList.forEach(item -> {
			item.setPay(true);
			cartItemService.updateIsPayToTrue(item);
		});

		return null;
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