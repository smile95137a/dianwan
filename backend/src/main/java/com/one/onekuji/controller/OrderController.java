//package com.one.onekuji.controller;
//
//import com.one.onekuji.service.OrderService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/order")
//public class OrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    // 创建订单
//    @PostMapping("/add")
//    public ResponseEntity<Order> createOrder(@RequestBody @Valid Order order) {
//        orderService.createOrder(order);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    // 获取所有订单
//    @GetMapping("/query")
//    public ResponseEntity<List<Order>> getAllOrders() {
//        List<Order> orders = orderService.getAllOrders();
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }
//
//    // 根据ID获取订单
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        Order order = orderService.getOrderById(id);
//        if (order != null) {
//            return new ResponseEntity<>(order, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // 更新订单
//    @PutMapping("/{id}")
//    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody @Valid Order order) {
//        orderService.updateOrder(id, order);
//            return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    // 删除订单
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
//        orderService.deleteOrder(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//}
