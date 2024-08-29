package com.one.onekuji.repository;

import com.one.onekuji.model.OrderDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    @Select("SELECT * FROM order_detail")
    List<OrderDetail> getAllOrderDetails();

    @Select("SELECT * FROM order_detail WHERE order_id = #{id}")
    OrderDetail getOrderDetailById(Long id);

    void deleteOrderDetail(Long id);
}
