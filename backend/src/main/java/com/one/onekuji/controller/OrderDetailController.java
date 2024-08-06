package com.one.onekuji.controller;

import com.one.onekuji.model.OrderDetail;
import com.one.onekuji.service.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderDetails")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Operation(summary = "创建订单详情", description = "创建新的订单详情记录")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "订单详情创建成功"),
            @ApiResponse(responseCode = "400", description = "无效的输入")
    })
    @PostMapping("/add")
    public ResponseEntity<OrderDetail> createOrderDetail(@RequestBody @Validated OrderDetail orderDetail) {
        orderDetailService.createOrderDetail(orderDetail);
        return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
    }

    @Operation(summary = "获取所有订单详情", description = "获取所有订单详情记录")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "订单详情获取成功")
    })
    @GetMapping("/query")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @Operation(summary = "根据ID获取订单详情", description = "根据ID获取订单详情记录")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "订单详情获取成功"),
            @ApiResponse(responseCode = "404", description = "订单详情未找到")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable Long id) {
        OrderDetail orderDetail = orderDetailService.getOrderDetailById(id);
        if (orderDetail != null) {
            return new ResponseEntity<>(orderDetail, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "更新订单详情", description = "更新现有的订单详情记录")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "订单详情更新成功"),
            @ApiResponse(responseCode = "404", description = "订单详情未找到")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDetail> updateOrderDetail(@PathVariable Long id, @RequestBody @Validated OrderDetail orderDetail) {
        orderDetailService.updateOrderDetail(id, orderDetail);
        return new ResponseEntity<>(orderDetail, HttpStatus.OK);
    }

    @Operation(summary = "删除订单详情", description = "删除现有的订单详情记录")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "订单详情删除成功"),
            @ApiResponse(responseCode = "404", description = "订单详情未找到")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.deleteOrderDetail(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
