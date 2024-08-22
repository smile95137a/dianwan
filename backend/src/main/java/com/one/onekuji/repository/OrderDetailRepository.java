//package com.one.onekuji.repository;
//
//import org.apache.ibatis.annotations.Insert;
//import org.apache.ibatis.annotations.Mapper;
//
//@Mapper
//public interface OrderDetailRepository {
//
//
//    @Insert("INSERT INTO order_detail (order_id, product_id, product_detail_id, product_detail_name, quantity, unit_price, total_price, result_status, result_item_id, bonus_points_earned) " +
//            "VALUES (#{orderId}, #{productId}, #{productDetailId}, #{productDetailName}, #{quantity}, #{unitPrice}, #{totalPrice}, #{resultStatus}, #{resultItemId}, #{bonusPointsEarned})")
//    void insertOrderDetail(OrderDetail orderDetail);
//}
