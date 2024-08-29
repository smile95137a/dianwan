package com.one.frontend.repository;

import com.one.frontend.dto.OrderDetailDto;
import com.one.frontend.request.StoreOrderDetailReq;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDetailRepository {


    @Insert("INSERT INTO order_detail (order_id, product_id, product_detail_name, quantity, unit_price, result_status) " +
            "VALUES (#{orderId}, #{productId}, #{productDetailName}, #{quantity}, #{unitPrice}, #{resultStatus})")
    void insertOrderDetailOne(OrderDetailDto orderDetail);


    @Insert({
            "<script>",
            "INSERT INTO order_detail (order_id, product_id, product_detail_name, quantity, unit_price) VALUES",
            "<foreach collection='orderDetailList' item='item' index='index' separator=','>",
            "(#{item.orderId}, #{item.productId}, #{item.productDetailName}, #{item.quantity}, #{item.unitPrice})",
            "</foreach>",
            "</script>"
    })
    void insertOrderDetail(@Param("orderDetailList") List<OrderDetailDto> orderDetailList);



    @Insert("INSERT INTO order_detail (order_id, store_product_id, store_product_name, quantity, unit_price, result_status, total_price) " +
            "VALUES (#{orderDetail.orderId}, #{orderDetail.storeProductId}, #{orderDetail.storeProductName}, " +
            "#{orderDetail.quantity}, #{orderDetail.unitPrice}, #{orderDetail.resultStatus}, #{orderDetail.totalPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "orderDetail.id")
    void save(@Param("orderDetail") StoreOrderDetailReq orderDetail);
}
