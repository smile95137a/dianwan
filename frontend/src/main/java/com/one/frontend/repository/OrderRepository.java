package com.one.frontend.repository;

import com.one.frontend.dto.DrawResultDto;
import com.one.frontend.model.Order;
import com.one.frontend.response.OrderRes;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderRepository {

	@Insert("INSERT INTO `order` (order_number, user_id, total_amount, shipping_cost, is_free_shipping, "
			+ "bonus_points_earned, bonus_points_used, created_at, updated_at, paid_at, result_status, "
			+ "payment_method, shipping_method, shipping_name, shipping_zip_code, shipping_city, shipping_area, "
			+ "shipping_address, billing_zip_code, billing_name, billing_city, billing_area, "
			+ "billing_address, invoice, tracking_number) "
			+ "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{shippingCost}, #{isFreeShipping}, "
			+ "#{bonusPointsEarned}, #{bonusPointsUsed}, #{createdAt}, #{updatedAt}, #{paidAt}, #{resultStatus}, "
			+ "#{paymentMethod}, #{shippingMethod}, #{shippingName}, #{shippingZipCode}, #{shippingCity}, #{shippingArea}, "
			+ "#{shippingAddress}, #{billingZipCode}, #{billingName}, #{billingCity}, #{billingArea}, "
			+ "#{billingAddress}, #{invoice}, #{trackingNumber})")
	void insertOrder(Order order);

	@Select("SELECT id FROM `order` WHERE order_number = #{orderNumber}")
	Long getOrderIdByOrderNumber(String orderNumber);

	@Select("SELECT * FROM `order` WHERE user_id = #{userId}")
	Order getOrderByUserId(Long userId);

	@Select("SELECT * FROM `order` WHERE user_id = #{userId} AND order_number = #{orderNumber}")
	OrderRes getOrderByUserIdAndOrderNumber(Long userId, String orderNumber);

	@Results(id = "orderResultMap", value = { 
			@Result(property = "id", column = "id"),
			@Result(property = "orderNumber", column = "order_number"),
			@Result(property = "totalAmount", column = "total_amount"),
			@Result(property = "bonusPointsEarned", column = "bonus_points_earned"),
			@Result(property = "bonusPointsUsed", column = "bonus_points_used"),
			@Result(property = "resultStatus", column = "result_status"),
			@Result(property = "createdAt", column = "created_at"), })
	@Select("SELECT o.* FROM `order` o WHERE o.order_number = #{orderNumber}")
	OrderRes findOrderByOrderNumber(String orderNumber);

	static String buildFindOrdersByDateRange(Map<String, Object> params) {
		Long userId = (Long) params.get("userId");
		LocalDateTime startDate = (LocalDateTime) params.get("startDate");
		LocalDateTime endDate = (LocalDateTime) params.get("endDate");

		var sql = new SQL() {
			{
				SELECT("*");
				FROM("`order`");
				if (userId != null) {
					WHERE("user_id = #{userId}");
				}
				if (startDate != null) {
					WHERE("created_at >= #{startDate}");
				}
				if (endDate != null) {
					WHERE("created_at <= #{endDate}");
				}
			}
		}.toString();

		return sql;
	}

	@SelectProvider(type = OrderRepository.class, method = "buildFindOrdersByDateRange")
	@ResultMap("orderResultMap")
	List<OrderRes> findOrdersByDateRange(Map<String, Object> params);

	@Select("SELECT a.* , b.product_name , b.image_urls FROM draw_result a left join product_detail b on a.product_detail_id = b.product_detail_id where draw_time >= #{startDate} and draw_time <= #{endDate} order by draw_time desc")
	List<DrawResultDto> queryDrawOrder(Object userId, Object startDate, Object endDate);
}
