package com.one.onekuji.service;

import com.one.onekuji.model.OrderDetail;
import com.one.onekuji.repository.OrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    public void createOrderDetail(OrderDetail orderDetail) {
        orderDetailMapper.insertOrderDetail(orderDetail);
    }

    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailMapper.getAllOrderDetails();
    }
    @Transactional(readOnly = true)
    public OrderDetail getOrderDetailById(Long id) {
        try{
            return orderDetailMapper.getOrderDetailById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void updateOrderDetail(Long id, OrderDetail orderDetail) {
        orderDetail.setId(id);
        orderDetailMapper.updateOrderDetail(orderDetail);
    }

    public void deleteOrderDetail(Long id) {
        orderDetailMapper.deleteOrderDetail(id);
    }
}
