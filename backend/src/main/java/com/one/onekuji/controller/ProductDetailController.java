package com.one.onekuji.controller;

import com.one.model.Product;
import com.one.model.ProductDetail;
import com.one.onekuji.request.ProductDetailReq;
import com.one.onekuji.request.ProductReq;
import com.one.onekuji.service.ProductDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productDetail")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @Operation(summary = "獲取所有獎品", description = "檢索所有獎品的列表")
    @GetMapping("/query")
    public ResponseEntity<List<ProductDetail>> getAllProduct() {
        List<ProductDetail> products = productDetailService.getAllProductDetail();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取獎品", description = "根據其 ID 獲取獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品檢索成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @GetMapping("/{productDetailId}")
    public ResponseEntity<ProductDetail> getProductById(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer productDetailId) {
        ProductDetail prize = productDetailService.getProductDetailById(productDetailId);
        if (prize != null) {
            return new ResponseEntity<>(prize, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "創建新的獎品", description = "創建一個新的獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "獎品創建成功"),
            @ApiResponse(responseCode = "400", description = "無效的輸入")
    })
    @PostMapping("/add")
    public ResponseEntity<String> createPrize(
            @Parameter(description = "要創建的獎品詳細信息") @RequestBody ProductDetailReq product) {
        String isSuccess = productDetailService.createProductDetail(product);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "更新現有獎品", description = "根據 ID 更新現有的獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品更新成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @PutMapping("/{productDetailId}")
    public ResponseEntity<String> updatePrize(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer productDetailId , @RequestBody ProductDetailReq productReq) {
        ProductDetail product = productDetailService.getProductDetailById(productDetailId);
        if (product != null) {
            String isSuccess = productDetailService.updateProductDetail(productReq);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失敗", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("獎品未找到", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "刪除獎品", description = "根據 ID 刪除獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品刪除成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @DeleteMapping("/{productDetailId}")
    public ResponseEntity<String> deleteProduct(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer productDetailId) {
        String isSuccess = productDetailService.deleteProductDetail(productDetailId);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("刪除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.BAD_REQUEST);
        }
    }
}
