package com.one.onekuji.repository;

import com.one.onekuji.model.Order;
import com.one.onekuji.response.OrderRes;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderRepository {
    @Insert("INSERT INTO `order` (order_number, user_id, total_amount, bonus_points_earned, bonus_points_used, status, payment_method, payment_status, created_at, updated_at, paid_at, notes) " +
            "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{bonusPointsEarned}, #{bonusPointsUsed}, #{status}, #{paymentMethod}, #{paymentStatus}, #{createdAt}, #{updatedAt}, #{paidAt}, #{notes})")
    void insertOrder(Order order);

    @Select("select id from `order` where order_number = #{orderNumber}")
    Long getOrderIdByOrderNumber(String orderNumber);

    @Select("SELECT * FROM `order`  WHERE id = #{id}")
    Order getOrderById(Long id);

    @Select("SELECT * FROM `order` ")
    List<Order> getAllOrders();

    @Update({
            "UPDATE `order`",
            "SET result_status = #{resultStatus},",
            "updated_at = #{now} " +
            "WHERE id = #{id}"
    })
    void updateOrder(@Param("id") Long id, @Param("resultStatus") String resultStatus , @Param("now") LocalDateTime now);

    @Delete("DELETE FROM `order`  WHERE id = #{id}")
    void deleteOrder(Long id);

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
        LocalDateTime startDate = (LocalDateTime) params.get("startDate");
        LocalDateTime endDate = (LocalDateTime) params.get("endDate");

        var sql = new SQL() {
            {
                SELECT("*");
                FROM("`order`");
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

}
