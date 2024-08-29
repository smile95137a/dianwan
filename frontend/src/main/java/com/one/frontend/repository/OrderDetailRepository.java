package com.one.frontend.repository;

import com.one.frontend.dto.OrderDetailDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailRepository {


    @Insert("INSERT INTO order_detail (order_id, product_id, product_detail_name, quantity, unit_price) " +
            "VALUES (#{orderId}, #{productId}, #{productDetailName}, #{quantity}, #{unitPrice})")
    void insertOrderDetail(OrderDetailDto orderDetail);
}
