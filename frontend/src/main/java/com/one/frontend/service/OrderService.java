package com.one.frontend.service;

import com.one.frontend.dto.DrawResultDto;
import com.one.frontend.ecpay.payment.integration.AllInOne;
import com.one.frontend.ecpay.payment.integration.domain.AioCheckOutALL;
import com.one.frontend.eenum.OrderStatus;
import com.one.frontend.model.*;
import com.one.frontend.repository.OrderDetailRepository;
import com.one.frontend.repository.OrderRepository;
import com.one.frontend.repository.StoreProductRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.OrderQueryReq;
import com.one.frontend.request.PayCartRes;
import com.one.frontend.request.ReceiptReq;
import com.one.frontend.response.*;
import com.one.frontend.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final CartItemService cartItemService;
	private final UserRepository userRepository;
	private final PaymentService paymentService;
	@Autowired
	private PrizeCartItemService prizeCartItemService;

	@Autowired
	private InvoiceService invoiceService;
	@Autowired
	private StoreProductRepository storeProductRepository;
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

	public List<OrderRes> queryOrders(Long userId, OrderQueryReq req) {
		// 初始化查询参数
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);

		// 处理开始日期和结束日期
		LocalDateTime startDate = convertToLocalDateTime(req.getStartDate());
		LocalDateTime endDate = convertToLocalDateTime(req.getEndDate());

		if (startDate != null) {
			startDate = startDate.with(LocalTime.MIN);
		}
		if (endDate != null) {
			endDate = endDate.with(LocalTime.MAX);
		}

		params.put("startDate", startDate);
		params.put("endDate", endDate);

		// 查询订单
		List<OrderRes> ordersByDateRange = orderRepository.findOrdersByDateRange(params);

		// 为每个订单填充订单详情
		List<OrderRes> list = ordersByDateRange.stream()
				.map(order -> {
					System.out.println(order);
					order.setOrderDetails(orderDetailRepository.findOrderDetailsByOrderId(order.getId()));
					return order;
				})
				.collect(Collectors.toList());

		// 处理订单状态描述
		List<OrderRes> orderStatusDescriptions = list.stream()
				.map(order -> {
					String statusDescription;
					switch (order.getResultStatus()) {
						case "PREPARING_SHIPMENT":
							statusDescription = "訂單準備中";
							break;
						case "SHIPPED":
							statusDescription = "已發貨";
							break;
						default:
							statusDescription = "未知狀態";
							break;
					}
					// 设置状态描述
					order.setResultStatus(statusDescription);
					return order;
				})
				.collect(Collectors.toList());

		return orderStatusDescriptions;
	}


	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
	    return dateToConvert == null ? null : LocalDateTime.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
	}

	@Transactional(rollbackFor = Exception.class)
	public String createOrder(PayCartRes payCartRes, List<CartItem> cartItemList, Long userId) throws Exception {

		// 生成訂單號
		String orderNumber = genOrderNumber();

		// 計算所有購物車商品的總價格
		BigDecimal totalProductAmount = cartItemList.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		// 計算運費，根據運輸方式動態設置
		BigDecimal shippingCost = calculateShippingCost(payCartRes.getShippingMethod());

		// 計算總金額，包括商品總價格和運費
		BigDecimal totalAmount = totalProductAmount.add(shippingCost);


		//取得用戶資訊
		UserRes userRes = userRepository.getUserById(userId);

		PaymentResponse paymentResponse = new PaymentResponse();
		if("1".equals(payCartRes.getPaymentMethod())){
			PaymentRequest paymentRequest = new PaymentRequest();
			BigDecimal totalAmount2 = new BigDecimal(String.valueOf(totalAmount)); // 假设你的 totalAmount 是 BigDecimal
			int amountToSend = totalAmount.setScale(0, BigDecimal.ROUND_DOWN).intValue(); // 去掉小数部分
			paymentRequest.setAmount(String.valueOf(amountToSend));
			paymentRequest.setBuyerName(payCartRes.getCardHolderName());
			paymentRequest.setBuyerTelm(userRes.getPhoneNumber());
			paymentRequest.setBuyerMail(userRes.getUsername());
			paymentRequest.setBuyerMemo("再來一抽備註");
			paymentRequest.setCardNo(payCartRes.getCardNo());
			paymentRequest.setExpireDate(payCartRes.getExpiryDate());
			paymentRequest.setCvv(payCartRes.getCvv());
			System.out.println(paymentRequest);
			paymentResponse = paymentService.creditCard(paymentRequest);
		}else if("2".equals(payCartRes.getPaymentMethod())){
//			paymentResponse = paymentResponse.webAtm(p)
		}


		//paymentResponse result = 1 等於成功
		if("1".equals(paymentResponse.getResult())){
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
		}else{
			throw new Exception("資料有錯" + paymentResponse.getRetMsg());
		}


		//訂單成立開立發票並且傳送至email
		ReceiptReq invoiceRequest = new ReceiptReq();
		if(payCartRes.getVehicle() != null){
			invoiceRequest.setOrderCode(payCartRes.getVehicle());
		}
		invoiceRequest.setEmail(userRes.getUsername());
		if(payCartRes.getState() != null){
			invoiceRequest.setState(1);
			invoiceRequest.setDonationCode(payCartRes.getDonationCode());
		}else{
			invoiceRequest.setState(0);
		}
		int amountToSend = totalAmount.setScale(0, BigDecimal.ROUND_DOWN).intValue(); // 去掉小数部分
		invoiceRequest.setTotalFee(String.valueOf(amountToSend));
		List<ReceiptReq.Item> items = new ArrayList<>();
		for(CartItem cartItem : cartItemList){
			ReceiptReq.Item item = new ReceiptReq.Item();
			StoreProduct byId = storeProductRepository.findById(cartItem.getStoreProductId());
			item.setName(byId.getProductName());
			item.setNumber(cartItem.getQuantity());
			item.setMoney(cartItem.getUnitPrice().intValue());
			items.add(item);
		}
		invoiceRequest.setItems(items);
		System.out.println("有到這");
		System.out.println(invoiceRequest);

		ResponseEntity<ReceiptRes> res = invoiceService.addB2CInvoice(invoiceRequest);
		System.out.println(res.getBody());
		ReceiptRes receiptRes = res.getBody();
		invoiceService.getInvoicePicture(receiptRes.getCode() , userId);


		return orderNumber; // 返回訂單號
	}

	@Transactional(rollbackFor = Exception.class)
	public String createPrizeOrder(PayCartRes payCartRes, List<PrizeCartItem> prizeCartItemList, Long userId) throws Exception {

		// 生成訂單號
		String orderNumber = genOrderNumber();

		// 計算運費，根據運輸方式動態設置
		BigDecimal shippingCost = calculateShippingCost(payCartRes.getShippingMethod());

		//取得用戶資訊
		UserRes userRes = userRepository.getUserById(userId);

//		PaymentResponse paymentResponse = new PaymentResponse();
//		if("1".equals(payCartRes.getPaymentMethod())){
//			PaymentRequest paymentRequest = new PaymentRequest();
//			BigDecimal totalAmount2 = new BigDecimal(String.valueOf(shippingCost)); // 假设你的 totalAmount 是 BigDecimal
//			int amountToSend = shippingCost.setScale(0, BigDecimal.ROUND_DOWN).intValue(); // 去掉小数部分
//			paymentRequest.setAmount(String.valueOf(amountToSend));
//			paymentRequest.setBuyerName(payCartRes.getCardHolderName());
//			paymentRequest.setBuyerTelm(userRes.getPhoneNumber());
//			paymentRequest.setBuyerMail(userRes.getUsername());
//			paymentRequest.setBuyerMemo("再來一抽備註");
//			paymentRequest.setCardNo(payCartRes.getCardNo());
//			paymentRequest.setExpireDate(payCartRes.getExpiryDate());
//			paymentRequest.setCvv(payCartRes.getCvv());
//			System.out.println(paymentRequest);
//			paymentResponse = paymentService.creditCard(paymentRequest);
//		}else if("2".equals(payCartRes.getPaymentMethod())){
////			paymentResponse = paymentResponse.webAtm(p)
//		}


		//paymentResponse result = 1 等於成功
//		if("1".equals(paymentResponse.getResult())){
			// 創建訂單實體，這裡包含了支付和運輸方式、收貨人、賬單信息等字段
			Order orderEntity = Order.builder().userId(userId).orderNumber(orderNumber).totalAmount(shippingCost) // 總金額 =
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
		System.out.println(prizeCartItemList);
			// 轉換購物車項目到訂單詳情並保存
		List<OrderDetail> orderDetails = prizeCartItemList.stream()
				.filter(Objects::nonNull)  // 過濾掉 null 元素
				.map(cartItem -> mapCartItemToPrizeOrderDetail(cartItem, orderId, shippingCost))
				.filter(Objects::nonNull)  // 過濾掉映射結果為 null 的元素
				.collect(Collectors.toList());

		// 批量保存訂單詳情
		if (!orderDetails.isEmpty()) {
			orderDetailRepository.savePrizeOrderDetailBatch(orderDetails);
		}

			// 獲取所有購物車項的ID並移除
			List<Long> cartItemIds = prizeCartItemList.stream().map(PrizeCartItem::getPrizeCartItemId).collect(Collectors.toList());

			// 移除購物車項
			prizeCartItemService.removeCartItems(cartItemIds, prizeCartItemList.get(0).getCartId());
//		}else{
//			throw new Exception("資料有錯" + paymentResponse.getRetMsg());
//		}


		//訂單成立開立發票並且傳送至email
//		ReceiptReq invoiceRequest = new ReceiptReq();
//		if(payCartRes.getVehicle() != null){
//			invoiceRequest.setOrderCode(payCartRes.getVehicle());
//		}
//		invoiceRequest.setEmail(userRes.getUsername());
//		if(payCartRes.getState() != null){
//			invoiceRequest.setState(1);
//			invoiceRequest.setDonationCode(payCartRes.getDonationCode());
//		}else{
//			invoiceRequest.setState(0);
//		}
//		int amountToSend = shippingCost.setScale(0, BigDecimal.ROUND_DOWN).intValue(); // 去掉小数部分
//		invoiceRequest.setTotalFee(String.valueOf(amountToSend));
//		List<ReceiptReq.Item> items = new ArrayList<>();
//		for(PrizeCartItem cartItem : prizeCartItemList){
//			ReceiptReq.Item item = new ReceiptReq.Item();
//			StoreProduct byId = storeProductRepository.findById(cartItem.getProductDetailId());
//			item.setName(byId.getProductName());
//			item.setNumber(cartItem.getQuantity());
//			item.setMoney(cartItem.getUnitPrice().intValue());
//			items.add(item);
//		}
//		invoiceRequest.setItems(items);
//		System.out.println("有到這");
//		System.out.println(invoiceRequest);
//
//		ResponseEntity<ReceiptRes> res = invoiceService.addB2CInvoice(invoiceRequest);
//		System.out.println(res.getBody());
//		ReceiptRes receiptRes = res.getBody();
//		invoiceService.getInvoicePicture(receiptRes.getCode() , userId);


		return orderNumber; // 返回訂單號
	}

	private Order createOrderEntity(PayCartRes payCartRes, Long userId, String orderNumber, BigDecimal shippingCost) {
		return Order.builder()
				.userId(userId)
				.orderNumber(orderNumber)
				.totalAmount(shippingCost)
				.paymentMethod(payCartRes.getPaymentMethod())
				.shippingCost(shippingCost)
				.shippingMethod(payCartRes.getShippingMethod())
				.shippingName(payCartRes.getShippingName())
				// ... 其他字段 ...
				.createdAt(LocalDateTime.now())
				.resultStatus(OrderStatus.PREPARING_SHIPMENT)
				.paidAt(LocalDateTime.now())
				.build();
	}



	private void removeCartItems(List<PrizeCartItem> prizeCartItemList) {
		List<Long> prizeCartItemIds = prizeCartItemList.stream()
				.map(PrizeCartItem::getPrizeCartItemId)
				.collect(Collectors.toList());

		prizeCartItemService.removeCartItems(prizeCartItemIds, prizeCartItemList.get(0).getCartId());
	}

	// 根據不同的運送方式計算運費
	private BigDecimal 	calculateShippingCost(String shippingMethodValue) {
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

	public OrderDetail mapCartItemToPrizeOrderDetail(PrizeCartItem cartItem, Long orderId , BigDecimal shippingCost) {

		return OrderDetail.builder().orderId(orderId).productDetailId(cartItem.getProductDetailId())
				.quantity(cartItem.getQuantity()).unitPrice(cartItem.getSliverPrice()).totalPrice(shippingCost) // 新增
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

	public OrderRes getPrizeOrderByOrderNumber(Long userId, String orderNumber) {
		try {
			var order = orderRepository.getOrderByUserIdAndOrderNumber(userId, orderNumber);
			if (order != null) {
				List<OrderDetailRes> orderDetails = orderDetailRepository.findPrizeOrderDetailsByOrderId(order.getId());
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

	public List<DrawResultDto> queryDrawOrder(Long userId, OrderQueryReq req) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);

		LocalDateTime startDate = convertToLocalDateTime(req.getStartDate());
		LocalDateTime endDate = convertToLocalDateTime(req.getEndDate());

		if (startDate != null) {
			startDate = startDate.with(LocalTime.MIN);
		}

		if (endDate != null) {
			endDate = endDate.with(LocalTime.MAX);
		}

		params.put("startDate", startDate);
		params.put("endDate", endDate);

		List<DrawResultDto> results = orderRepository.queryDrawOrder(params.get("userId"),
				params.get("startDate"),
				params.get("endDate"));

		// 过滤掉 productName 和 imageUrls 为 null 的项
		if (results != null) {
			results = results.stream()
					.filter(result -> result.getProductName() != null && result.getImageUrls() != null)
					.collect(Collectors.toList());
		} else {
			results = new ArrayList<>();
		}

		return results;
	}


}