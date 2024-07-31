package com.one.frontend.controller;

import com.one.frontend.model.ProductDetail;
import com.one.frontend.service.ProductDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productDetail")
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

}
