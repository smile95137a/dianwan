package com.one.frontend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import com.one.frontend.dto.OrderDto;
import com.one.frontend.model.Order;
import com.one.frontend.response.OrderRes;

@Mapper
public interface OrderRepository {

	@Insert("INSERT INTO `order` (order_number, user_id, total_amount, created_at, result_status) "
			+ "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{createdAt}, #{resultStatus})")
	void insertOrder(OrderDto order);

	@Select("SELECT id FROM `order` WHERE order_number = #{orderNumber}")
	Long getOrderIdByOrderNumber(String orderNumber);

	@Select("SELECT * FROM `order` WHERE user_uid = #{userUid}")
	Order getOrderById(String userUid);

	@Insert("INSERT INTO `order` (order_number, user_id, total_amount, bonus_points_earned, bonus_points_used, created_at, updated_at, paid_at, result_status) "
			+ "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{bonusPointsEarned}, #{bonusPointsUsed}, #{createdAt}, #{updatedAt}, #{paidAt}, #{resultStatus}) "
			+ "ON DUPLICATE KEY UPDATE " + "user_id = #{userId}, " + "total_amount = #{totalAmount}, "
			+ "bonus_points_earned = #{bonusPointsEarned}, " + "bonus_points_used = #{bonusPointsUsed}, "
			+ "created_at = #{createdAt}, " + "updated_at = #{updatedAt}, " + "paid_at = #{paidAt}, "
			+ "result_status = #{resultStatus}")
	void save(Order order);

	@Results(id = "orderResultMap", value = {
		    @Result(property = "orderId", column = "id"),
		    @Result(property = "orderNumber", column = "order_number"),
		    @Result(property = "userId", column = "user_id"),
		    @Result(property = "totalAmount", column = "total_amount"),
		    @Result(property = "bonusPointsEarned", column = "bonus_points_earned"),
		    @Result(property = "bonusPointsUsed", column = "bonus_points_used"),
		    @Result(property = "resultStatus", column = "result_status"),
		    @Result(property = "createdAt", column = "created_at"),
		})
	@Select("SELECT o.* FROM `order` o WHERE o.order_number = #{orderNumber}")
	OrderRes findOrderByOrderNumber(String orderNumber);

	static String buildFindOrdersByDateRange(Map<String, Object> params) {
        Long userId = (Long) params.get("userId");
        LocalDateTime startDate = (LocalDateTime) params.get("startDate");
        LocalDateTime endDate = (LocalDateTime) params.get("endDate");

        var sql =    new SQL() {{
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
        }}.toString();

        return sql;
    }

	@SelectProvider(type = OrderRepository.class, method = "buildFindOrdersByDateRange")
	@ResultMap("orderResultMap")
	List<OrderRes> findOrdersByDateRange(Map<String, Object> params);
}
