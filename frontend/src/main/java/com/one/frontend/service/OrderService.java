package com.one.frontend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.one.frontend.ecpay.payment.integration.AllInOne;
import com.one.frontend.ecpay.payment.integration.domain.AioCheckOutALL;
import com.one.frontend.eenum.OrderStatus;
import com.one.frontend.model.CartItem;
import com.one.frontend.model.Order;
import com.one.frontend.model.OrderDetail;
import com.one.frontend.repository.OrderDetailRepository;
import com.one.frontend.repository.OrderRepository;
import com.one.frontend.request.PayCartRes;
import com.one.frontend.response.OrderDetailRes;
import com.one.frontend.response.OrderRes;
import com.one.frontend.util.RandomUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final CartItemService cartItemService;

	public String ecpayCheckout(Integer userId) {

		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

		AllInOne all = new AllInOne("");

		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate("2017/01/01 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		obj.setReturnURL("https://2217-111-248-73-184.ngrok-free.app/returnUrl");
		obj.setNeedExtraPaidInfo("N");
		obj.setCustomField1(String.valueOf(userId));
		String form = all.aioCheckOut(obj, null);
		System.out.println(form);
		return form;
	}

//	public List<OrderRes> queryOrders(Long userId, OrderQueryReq req) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("userId", userId);
//
//		LocalDateTime startDate = convertToLocalDateTime(req.getStartDate());
//		LocalDateTime endDate = convertToLocalDateTime(req.getEndDate());
//
//		// 调整 startDate 和 endDate 的时间部分
//		if (startDate != null) {
//			startDate = startDate.with(LocalTime.MIN); 
//		}
//		if (endDate != null) {
//			endDate = endDate.with(LocalTime.MAX); 
//		}
//
//		params.put("startDate", startDate);
//		params.put("endDate", endDate);
//
//		return orderRepository.findOrdersByDateRange(params).stream().peek(
//				order -> order.setOrderDetails(orderDetailRepository.findOrderDetailsByOrderId(order.getOrderId())))
//				.toList();
//	}
//	
//	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
//	    return dateToConvert == null ? null : LocalDateTime.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
//	}

	@Transactional(rollbackFor = Exception.class)
	public String createOrder(PayCartRes payCartRes, List<CartItem> cartItemList, Long userId) {

		// 生成訂單號
		String orderNumber = genOrderNumber();

		// 計算所有購物車商品的總價格
		BigDecimal totalProductAmount = cartItemList.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		// 計算運費，根據運輸方式動態設置
		BigDecimal shippingCost = calculateShippingCost(payCartRes.getShippingMethod());

		// 計算總金額，包括商品總價格和運費
		BigDecimal totalAmount = totalProductAmount.add(shippingCost);

		// 創建訂單實體，這裡包含了支付和運輸方式、收貨人、賬單信息等字段
		Order orderEntity = Order.builder().userId(userId).orderNumber(orderNumber).totalAmount(totalAmount) // 總金額 =
																												// 商品價格
																												// + 運費
				.paymentMethod(payCartRes.getPaymentMethod()) // 從 PayCartRes 獲取支付方式
				.shippingCost(shippingCost) // 保存運費
				.shippingMethod(payCartRes.getShippingMethod()) // 從 PayCartRes 獲取運送方式
				.shippingName(payCartRes.getShippingName()) // 從 PayCartRes 獲取收貨人姓名
				.shippingEmail(payCartRes.getShippingEmail()) // 從 PayCartRes 獲取收貨人 Email
				.shippingPhone(payCartRes.getShippingPhone()) // 從 PayCartRes 獲取收貨人電話
				.shippingZipCode(payCartRes.getShippingZipCode()) // 從 PayCartRes 獲取收貨地址郵遞區號
				.shippingCity(payCartRes.getShippingCity()) // 從 PayCartRes 獲取收貨城市
				.shippingArea(payCartRes.getShippingArea()) // 從 PayCartRes 獲取收貨區域
				.shippingAddress(payCartRes.getShippingAddress()) // 從 PayCartRes 獲取收貨地址
				.billingName(payCartRes.getBillingName()) // 從 PayCartRes 獲取賬單姓名
				.billingEmail(payCartRes.getBillingEmail()) // 從 PayCartRes 獲取賬單 Email
				.billingPhone(payCartRes.getBillingPhone()) // 從 PayCartRes 獲取賬單電話
				.billingZipCode(payCartRes.getBillingZipCode()) // 從 PayCartRes 獲取賬單地址郵遞區號
				.billingCity(payCartRes.getBillingCity()) // 從 PayCartRes 獲取賬單城市
				.billingArea(payCartRes.getBillingArea()) // 從 PayCartRes 獲取賬單區域
				.billingAddress(payCartRes.getBillingAddress()) // 從 PayCartRes 獲取賬單地址
				.invoice(payCartRes.getInvoice()) // 從 PayCartRes 獲取發票信息
				.createdAt(LocalDateTime.now()).resultStatus(OrderStatus.PREPARING_SHIPMENT) // 訂單狀態設為準備發貨
				.paidAt(LocalDateTime.now()) // 假設已付款，更新付款時間
				.build();

		// 插入訂單到資料庫
		orderRepository.insertOrder(orderEntity);

		// 根據訂單號查詢訂單ID
		Long orderId = orderRepository.getOrderIdByOrderNumber(orderNumber);

		// 轉換購物車項目到訂單詳情並保存
		cartItemList.stream().map(cartItem -> mapCartItemToOrderDetail(cartItem, orderId)) // 映射購物車項目為訂單詳情
				.forEach(orderDetail -> orderDetailRepository.saveOrderDetail(orderDetail)); // 保存訂單詳情

		// 獲取所有購物車項的ID並移除
		List<Long> cartItemIds = cartItemList.stream().map(CartItem::getCartItemId).collect(Collectors.toList());

		// 移除購物車項
		cartItemService.removeCartItems(cartItemIds, cartItemList.get(0).getCartId());

		return orderNumber; // 返回訂單號
	}

	// 根據不同的運送方式計算運費
	private BigDecimal calculateShippingCost(String shippingMethodValue) {
		switch (shippingMethodValue) {
		case "homeDelivery":
			return new BigDecimal("160"); // 宅配運費
		case "sevenEleven":
		case "familyMart":
		case "hilife":
			return new BigDecimal("60"); // 超商取貨運費
		case "postOffice":
			return new BigDecimal("100"); // 郵寄運費
		default:
			return BigDecimal.ZERO; // 未知方式，設置為0
		}
	}

	public String genOrderNumber() {
		var orderDate = LocalDateTime.now();
		var orderDateStr = orderDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		var orderSerial = RandomUtils.genRandomNumbers(24);
		var orderNumber = String.format("%s%s", orderDateStr, orderSerial);
		return orderNumber;
	}

	public OrderDetail mapCartItemToOrderDetail(CartItem cartItem, Long orderId) {
		BigDecimal totalPrice = cartItem.getUnitPrice().multiply(new BigDecimal(cartItem.getQuantity()));

		return OrderDetail.builder().orderId(orderId).storeProductId(cartItem.getStoreProductId())
				.quantity(cartItem.getQuantity()).unitPrice(cartItem.getUnitPrice()).totalPrice(totalPrice) // 新增
																											// totalPrice
				.build();
	}

	public OrderRes getOrderByOrderNumber(Long userId, String orderNumber) {
		try {
			var order = orderRepository.getOrderByUserIdAndOrderNumber(userId, orderNumber);
			if (order != null) {
				List<OrderDetailRes> orderDetails = orderDetailRepository.findOrderDetailsByOrderId(order.getId());
				order.setOrderDetails(orderDetails);
			}

			return order;
		} catch (Exception e) {
			// 在这里处理异常，例如记录日志或抛出自定义异常
			System.err.println("Error retrieving order by order number: " + e.getMessage());
			// 您可以选择抛出自定义异常，或者返回一个默认值（如null）
			return null; // 或者 throw new CustomException("Failed to retrieve order", e);
		}
	}

}