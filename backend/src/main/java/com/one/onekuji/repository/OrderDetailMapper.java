//package com.one.onekuji.repository;
//
//import com.one.onekuji.model.OrderDetail;
//import org.apache.ibatis.annotations.*;
//
//import java.util.List;
//
//@Mapper
//public interface OrderDetailMapper {
//
//    @Insert("INSERT INTO order_detail (id, product_id, product_detail_id, product_detail_name, quantity, unit_price, total_price, result_status, result_item_id, bonus_points_earned) VALUES (#{id}, #{productId}, #{productDetailId}, #{productDetailName}, #{quantity}, #{unitPrice}, #{totalPrice}, #{resultStatus}, #{resultItemId}, #{bonusPointsEarned})")
//    void insertOrderDetail(OrderDetail orderDetail);
//
//    @Select("SELECT * FROM order_detail")
//    List<OrderDetail> getAllOrderDetails();
//
//    @Select("SELECT * FROM order_detail WHERE order_id = #{id}")
//    OrderDetail getOrderDetailById(Long id);
//
//    @Update("UPDATE order_detail SET id = #{id}, product_id = #{productId}, product_detail_id = #{productDetailId}, product_detail_name = #{productDetailName}, quantity = #{quantity}, unit_price = #{unitPrice}, total_price = #{totalPrice}, result_status = #{resultStatus}, result_item_id = #{resultItemId}, bonus_points_earned = #{bonusPointsEarned} WHERE id = #{id}")
//    void updateOrderDetail(OrderDetail orderDetail);
//
//    @Delete("DELETE FROM order_detail WHERE id = #{id}")
//    void deleteOrderDetail(Long id);
//}
