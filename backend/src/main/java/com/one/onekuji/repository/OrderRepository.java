package com.one.onekuji.repository;

import com.one.onekuji.model.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
            "SET result_status = #{order.resultStatus},",
            "updated_at = #{order.updatedAt},",
            "payment_method = #{order.paymentMethod},",
            "shipping_name = #{order.shippingName},",
            "shipping_email = #{order.shippingEmail},",
            "shipping_phone = #{order.shippingPhone},",
            "shipping_zip_code = #{order.shippingZipCode},",
            "shipping_city = #{order.shippingCity},",
            "shipping_area = #{order.shippingArea},",
            "shipping_address = #{order.shippingAddress},",
            "billing_name = #{order.billingName},",
            "billing_email = #{order.billingEmail},",
            "billing_phone = #{order.billingPhone},",
            "billing_zip_code = #{order.billingZipCode},",
            "billing_city = #{order.billingCity},",
            "billing_area = #{order.billingArea},",
            "billing_address = #{order.billingAddress},",
            "invoice = #{order.invoice},",
            "tracking_number = #{order.trackingNumber}",
            "WHERE id = #{id}"
    })
    void updateOrder(@Param("id") Long id, @Param("order") Order order);

    @Delete("DELETE FROM `order`  WHERE id = #{id}")
    void deleteOrder(Long id);
}
