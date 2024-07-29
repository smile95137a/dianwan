package com.one.repository;

import com.one.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderRepository {
    @Insert("INSERT INTO `order` (order_number, user_id, total_amount, bonus_points_earned, bonus_points_used, status, payment_method, payment_status, created_at, updated_at, paid_at, notes) " +
            "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{bonusPointsEarned}, #{bonusPointsUsed}, #{status}, #{paymentMethod}, #{paymentStatus}, #{createdAt}, #{updatedAt}, #{paidAt}, #{notes})")
    void insertOrder(Order order);

    @Select("select id from `order` where order_number = #{orderNumber}")
    Long getOrderIdByOrderNumber(String orderNumber);
}
