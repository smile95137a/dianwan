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

}
