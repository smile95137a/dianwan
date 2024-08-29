package com.one.frontend.repository;

import com.one.frontend.dto.OrderDto;
import com.one.frontend.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderRepository {
    @Insert("INSERT INTO `order` (order_number, user_id, total_amount, created_at, result_status) " +
            "VALUES (#{orderNumber}, #{userId}, #{totalAmount}, #{createdAt}, #{resultStatus})")
    void insertOrder(OrderDto order);


    @Select("select id from `order` where order_number = #{orderNumber}")
    Long getOrderIdByOrderNumber(String orderNumber);
    @Select("select * from `order` where user_uid = #{userUid}")
    Order getOrderById(String userUid);

    void save(Order order);
}
