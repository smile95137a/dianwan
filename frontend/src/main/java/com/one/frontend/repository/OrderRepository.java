package com.one.frontend.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.one.frontend.dto.OrderDto;
import com.one.frontend.model.Order;
import com.one.frontend.response.OrderRes;

@Mapper
public interface OrderRepository {
    @Insert("INSERT INTO `order` (order_number, user_id, total_amount, created_at, result_status) " +
            "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{createdAt}, #{resultStatus})")
    void insertOrder(OrderDto order);


    @Select("select id from `order` where order_number = #{orderNumber}")
    Long getOrderIdByOrderNumber(String orderNumber);
    @Select("select * from `order` where user_uid = #{userUid}")
    Order getOrderById(String userUid);

    
    @Insert(
    	    "INSERT INTO `order` (order_number, user_id, total_amount, bonus_points_earned, bonus_points_used, created_at, updated_at, paid_at, result_status) " +
    	    "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{bonusPointsEarned}, #{bonusPointsUsed}, #{createdAt}, #{updatedAt}, #{paidAt}, #{resultStatus}) " +
    	    "ON DUPLICATE KEY UPDATE " +
    	    "user_id = #{userId}, " +
    	    "total_amount = #{totalAmount}, " +
    	    "bonus_points_earned = #{bonusPointsEarned}, " +
    	    "bonus_points_used = #{bonusPointsUsed}, " +
    	    "created_at = #{createdAt}, " +
    	    "updated_at = #{updatedAt}, " +
    	    "paid_at = #{paidAt}, " +
    	    "result_status = #{resultStatus}"
    	)
	void save(Order order);


    @Select("SELECT o.* " +
            "FROM `order` o " +
            "WHERE o.order_number = #{orderNumber}")
    @Results({
        @Result(property = "orderId", column = "id"),
        @Result(property = "orderNumber", column = "order_number"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "totalAmount", column = "total_amount"),
        @Result(property = "bonusPointsEarned", column = "bonus_points_earned"),
        @Result(property = "bonusPointsUsed", column = "bonus_points_used"),
        @Result(property = "resultStatus", column = "result_status"),
    })
    OrderRes findOrderByOrderNumber(String orderNumber);


}
