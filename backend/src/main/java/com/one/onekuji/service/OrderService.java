package com.one.onekuji.service;

import com.one.onekuji.model.Order;
import com.one.onekuji.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderMapper;

    public void createOrder(Order order) {
        orderMapper.insertOrder(order);
    }

    public Order getOrderById(Long id) {
        return orderMapper.getOrderById(id);
    }

    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    public void updateOrder(Long id ,Order order) {
        orderMapper.updateOrder(order);
    }

    public void deleteOrder(Long id) {
        orderMapper.deleteOrder(id);
    }
}
