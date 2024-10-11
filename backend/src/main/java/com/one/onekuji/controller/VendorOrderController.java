package com.one.onekuji.controller;

import com.one.onekuji.model.ApiResponse;
import com.one.onekuji.model.VendorOrderEntity;
import com.one.onekuji.repository.VendorOrderRepository;
import com.one.onekuji.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/vendorOrder")
public class VendorOrderController {

    @Autowired
    private VendorOrderRepository vendorOrderRepository;

    @GetMapping(value = "/all")
    public ResponseEntity<ApiResponse<List<VendorOrderEntity>>> getAllCategory() {
        List<VendorOrderEntity> categories = vendorOrderRepository.findAll();
        if (categories == null || categories.isEmpty()) {
            ApiResponse<List<VendorOrderEntity>> response = ResponseUtils.failure(404, "無類別", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<List<VendorOrderEntity>> response = ResponseUtils.success(200, null, categories);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<VendorOrderEntity> updateOrder(@PathVariable String id, @RequestBody VendorOrderEntity vendorOrder) {
        // 在此处可以添加一些验证逻辑，比如检查 ID 和订单是否匹配
        vendorOrder.setVendorOrder(id); // 确保将 ID 设置到 vendorOrder 实体中
        vendorOrderRepository.update(vendorOrder);

        return ResponseUtils.success(200, "訂單更新成功", vendorOrder);
    }



}
