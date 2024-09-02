package com.one.frontend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.one.frontend.request.OrderQueryReq;
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

	public Order getOrderById(String userUid) {
		return orderRepository.getOrderById(userUid);
	}

	public List<OrderRes> queryOrders(Long userId, OrderQueryReq req) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);

		LocalDateTime startDate = convertToLocalDateTime(req.getStartDate());
		LocalDateTime endDate = convertToLocalDateTime(req.getEndDate());

		// 调整 startDate 和 endDate 的时间部分
		if (startDate != null) {
			startDate = startDate.with(LocalTime.MIN); 
		}
		if (endDate != null) {
			endDate = endDate.with(LocalTime.MAX); 
		}

		params.put("startDate", startDate);
		params.put("endDate", endDate);

		return orderRepository.findOrdersByDateRange(params).stream().peek(
				order -> order.setOrderDetails(orderDetailRepository.findOrderDetailsByOrderId(order.getOrderId())))
				.toList();
	}
	private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
	    return dateToConvert == null ? null : LocalDateTime.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
	}
	public OrderRes getOrderByOrderNumber(String orderNumber) {

		OrderRes order = orderRepository.findOrderByOrderNumber(orderNumber);
		if (order != null) {
			List<OrderDetailRes> orderDetails = orderDetailRepository.findOrderDetailsByOrderId(order.getOrderId());
			order.setOrderDetails(orderDetails);
		}
		return order;
	}

	public void save(Order order) {
		orderRepository.save(order);
	}

	@Transactional(rollbackFor = Exception.class)
	public String createOrder(List<CartItem> cartItemList, Long userId) {
		var totalAmount = cartItemList.stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

		var orderNumber = RandomUtils.genRandomNumbers(32);
		var order = Order.builder().orderNumber(orderNumber).userId(userId).totalAmount(totalAmount)
				.createdAt(LocalDateTime.now()).resultStatus(OrderStatus.PREPARING_SHIPMENT).paidAt(LocalDateTime.now())
				.build();
		orderRepository.save(order);

		Long orderId = orderRepository.getOrderIdByOrderNumber(orderNumber);

		for (CartItem cartItem : cartItemList) {
			var orderDetail = OrderDetail.builder().orderId(orderId).storeProductId(cartItem.getStoreProductId())
					.quantity(cartItem.getQuantity()).unitPrice(cartItem.getUnitPrice())
					.resultStatus(OrderStatus.PREPARING_SHIPMENT).build();
			orderDetailRepository.saveOrderDetail(orderDetail);
		}

		var cartItemIds = cartItemList.stream().map(CartItem::getCartItemId).collect(Collectors.toList());
		cartItemService.removeCartItems(cartItemIds, cartItemList.get(0).getCartId());
		return orderNumber;
	}
}