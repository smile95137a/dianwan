package com.one.repository;

import com.one.model.OrderDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailRepository {

    @Insert("INSERT INTO order_detail (order_id, prize_id, prize_detail_id, prize_detail_name, blind_box_id, blind_box_name, gacha_id, gacha_name, quantity, unit_price, total_price, result_status, result_item_id, bonus_points_earned) " +
            "VALUES (#{orderId}, #{prizeId}, #{prizeDetailId}, #{prizeDetailName}, #{blindBoxId}, #{blindBoxName}, #{gachaId}, #{gachaName}, #{quantity}, #{unitPrice}, #{totalPrice}, #{resultStatus}, #{resultItemId}, #{bonusPointsEarned})")
    void insertOrderDetail(OrderDetail orderDetail);
}
