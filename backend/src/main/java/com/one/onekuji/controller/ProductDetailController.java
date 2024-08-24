package com.one.onekuji.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.onekuji.model.ApiResponse;
import com.one.onekuji.request.DetailReq;
import com.one.onekuji.response.DetailRes;
import com.one.onekuji.service.ProductDetailService;
import com.one.onekuji.util.ImageUtil;
import com.one.onekuji.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/productDetail")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping(value = "/all")
    public ResponseEntity<ApiResponse<List<DetailRes>>> getAllProductDetails() {
        List<DetailRes> productDetailResList = productDetailService.getAllProductDetails();
        if (productDetailResList == null || productDetailResList.isEmpty()) {
            ApiResponse<List<DetailRes>> response = ResponseUtils.failure(404, "無商品", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<List<DetailRes>> response = ResponseUtils.success(200, null, productDetailResList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<List<DetailRes>>> addProductDetails(
            @RequestParam("productDetailReqs") String productDetailReqsJson,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        List<DetailReq> detailReqs = new ObjectMapper().readValue(productDetailReqsJson, new TypeReference<List<DetailReq>>() {});

        Map<Integer, String> imageUrlsMap = new HashMap<>();

        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                if (!image.isEmpty() && i < detailReqs.size()) {
                    String fileUrl = ImageUtil.upload(image);
                    imageUrlsMap.put(i, fileUrl);
                }
            }
        }

        for (int i = 0; i < detailReqs.size(); i++) {
            DetailReq detailReq = detailReqs.get(i);
            String imageUrl = imageUrlsMap.get(i);
            if (imageUrl != null) {
                detailReq.setImageUrl(imageUrl);
            }
        }

        List<DetailRes> detailResList = productDetailService.addProductDetails(detailReqs);

        ApiResponse<List<DetailRes>> response = ResponseUtils.success(201, null, detailResList);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping(value = "/update/{id}")
    public ResponseEntity<ApiResponse<DetailRes>> updateProductDetail(
            @PathVariable Long id,
            @RequestPart("productDetailReq") String productDetailReqJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws JsonProcessingException {
        DetailReq productDetailReq = new ObjectMapper().readValue(productDetailReqJson, DetailReq.class);
        if (productDetailReq == null) {
            ApiResponse<DetailRes> response = ResponseUtils.failure(404, "未找到該商品", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<String> fileUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String fileUrl = ImageUtil.upload(image);
                    fileUrls.add(fileUrl);
                }
            }
        }

        productDetailReq.setImageUrl(String.join(",", fileUrls));
        DetailRes productDetailRes = productDetailService.updateProductDetail(id, productDetailReq);
        ApiResponse<DetailRes> response = ResponseUtils.success(200, "商品已成功更新", productDetailRes);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductDetail(@PathVariable Long id) {
        boolean isDeleted = productDetailService.deleteProductDetail(id);
        if (!isDeleted) {
            ApiResponse<Void> response = ResponseUtils.failure(404, "未找到該商品", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<Void> response = ResponseUtils.success(200, "商品已成功刪除", null);
        return ResponseEntity.ok(response);
    }
}
