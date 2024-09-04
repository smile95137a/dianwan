package com.one.frontend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.response.StoreProductRes;
import com.one.frontend.service.StoreProductService;
import com.one.frontend.util.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/storeProduct")
public class StoreProductController {

    @Autowired
    private StoreProductService storeProductService;

    @Operation(summary = "獲取所有產品", description = "檢索所有產品的列表")
    @GetMapping("/query")
    public ResponseEntity<?> getAllStoreProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        List<StoreProductRes> products = storeProductService.getAllStoreProducts(page, size);
        if (products == null || products.isEmpty()) {
            var res = ResponseUtils.failure(999, "無產品", null);
            return ResponseEntity.ok(res);
        }

        var res = ResponseUtils.success(000, null, products);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/query/{productCode}")
    public ResponseEntity<?> getProductById(@PathVariable String productCode) {
        StoreProductRes productRes = storeProductService.getStoreProductByProductCode(productCode);
        if (productRes == null) {
            ApiResponse<StoreProductRes> response = ResponseUtils.failure(404, "產品不存在", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<StoreProductRes> response = ResponseUtils.success(200, null, productRes);
        return ResponseEntity.ok(response);
    }
}
