package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.response.StoreProductRes;
import com.one.frontend.service.StoreProductService;
import com.one.frontend.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storeProduct")
public class StoreProductController {

    @Autowired
    private StoreProductService storeProductService;

    @Operation(summary = "獲取所有產品", description = "檢索所有產品的列表")
    @GetMapping("/query")
    public ResponseEntity<ApiResponse<List<StoreProductRes>>> getAllStoreProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        List<StoreProductRes> products = storeProductService.getAllStoreProducts(page, size);
        if (products == null || products.isEmpty()) {
            ApiResponse<List<StoreProductRes>> response = ResponseUtils.failure(404, "無產品", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<List<StoreProductRes>> response = ResponseUtils.success(200, null, products);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "獲取產品詳情", description = "通過產品 ID 獲取產品的詳細信息")
    @GetMapping("/query/{id}")
    public ResponseEntity<ApiResponse<StoreProductRes>> getProductById(@PathVariable Long id) {
        StoreProductRes productRes = storeProductService.getStoreProductById(id);
        if (productRes == null) {
            ApiResponse<StoreProductRes> response = ResponseUtils.failure(404, "產品不存在", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<StoreProductRes> response = ResponseUtils.success(200, null, productRes);
        return ResponseEntity.ok(response);
    }
}
