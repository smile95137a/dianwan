package com.one.onekuji.controller;

import com.one.onekuji.model.ApiResponse;
import com.one.onekuji.model.VendorOrderEntity;
import com.one.onekuji.repository.VendorOrderRepository;
import com.one.onekuji.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
