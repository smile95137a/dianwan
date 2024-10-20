package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.response.ProductDetailRes;
import com.one.frontend.response.ProductRes;
import com.one.frontend.service.ProductDetailService;
import com.one.frontend.service.ProductService;
import com.one.frontend.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Operation(summary = "獲取所有獎品", description = "檢索所有獎品的列表")
    @GetMapping("/query")
    public ResponseEntity<ApiResponse<List<ProductRes>>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        List<ProductRes> products = productService.getAllProduct(page, size);
        List<ProductDetailRes> productDetailByProductId = productDetailService.getProductDetailByProductId(Long.valueOf(products.get(0).getProductId()));
        if (products == null || products.isEmpty()) {
            ApiResponse<List<ProductRes>> response = ResponseUtils.failure(404, "無類別", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<List<ProductRes>> response = ResponseUtils.success(200, null, products);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "獲取產品詳情", description = "通過產品 ID 獲取產品的詳細信息")
    @GetMapping("/query/{id}")
    public ResponseEntity<ApiResponse<ProductRes>> getProductById(@PathVariable Long id) {
        ProductRes productRes = productService.getProductById(id);
        if (productRes == null) {
            ApiResponse<ProductRes> response = ResponseUtils.failure(404, "產品不存在", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<ProductRes> response = ResponseUtils.success(200, null, productRes);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "獲取產品詳情", description = "通過產品 ID 獲取產品的詳細信息")
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<ProductRes>>> getProductByType(@PathVariable String type) {
        List<ProductRes> productRes = productService.getProductByType(type);
        if (productRes == null) {
            ApiResponse<List<ProductRes>> response = ResponseUtils.failure(404, "產品不存在", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<List<ProductRes>> response = ResponseUtils.success(200, null, productRes);
        return ResponseEntity.ok(response);
    }

}
