package com.one.onekuji.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.onekuji.model.ProductDetail;
import com.one.onekuji.request.ProductDetailReq;
import com.one.onekuji.service.ProductDetailService;
import com.one.onekuji.util.ImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @PostMapping("/add")
    public ResponseEntity<String> createPrize(
            @Parameter(description = "要创建的奖品详细信息") @RequestPart(value = "image", required = false) MultipartFile image,
            @Parameter(description = "奖品详细信息") @RequestPart("productDetailReq") String productDetailReqJson) throws JsonProcessingException {

        ProductDetailReq productDetailReq = new ObjectMapper().readValue(productDetailReqJson, ProductDetailReq.class);
        String fileUrl = null;
        if (image != null && !image.isEmpty()) {
            // 只有在图像非空时才进行上传
            fileUrl = ImageUtil.upload(image);
        } else {
            // 如果图像为空，可以选择设置默认图片 URL 或者处理其他逻辑
            fileUrl = "default-image-url"; // 假设这里设置了一个默认的图片 URL
        }

        productDetailReq.setImage(fileUrl);

        String isSuccess = productDetailService.createProductDetail(productDetailReq);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("创建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("创建失败", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "更新现有奖品", description = "根据 ID 更新现有的奖品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "奖品更新成功"),
            @ApiResponse(responseCode = "404", description = "奖品未找到")
    })
    @PutMapping("/{productDetailId}")
    public ResponseEntity<String> updatePrize(
            @Parameter(description = "奖品的 ID", example = "1") @PathVariable Integer productDetailId,
            @Parameter(description = "奖品图片") @RequestPart(value = "image", required = false) MultipartFile image,
            @Parameter(description = "奖品详细信息") @RequestPart("productDetailReq") String productDetailReqJson) throws JsonProcessingException {

        ProductDetailReq productDetailReq = new ObjectMapper().readValue(productDetailReqJson, ProductDetailReq.class);

        String fileUrl = null;
        if (image != null && !image.isEmpty()) {
            // 只有在图像非空时才进行上传
            fileUrl = ImageUtil.upload(image);
        } else {
            // 如果图像为空，可以选择设置默认图片 URL 或者处理其他逻辑
            fileUrl = "default-image-url"; // 假设这里设置了一个默认的图片 URL
        }
        productDetailReq.setImage(fileUrl);
            String isSuccess = productDetailService.updateProductDetail(productDetailId, productDetailReq);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失败", HttpStatus.BAD_REQUEST);
            }
    }


    private String saveImage(MultipartFile file) {
        // 确保文件名安全
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // 创建文件存储路径
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 目标文件路径
            Path filePath = uploadPath.resolve(fileName);

            // 保存文件到目标路径
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 构造图片的 URL
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();
        } catch (IOException e) {
            throw new RuntimeException("无法保存文件: " + e.getMessage(), e);
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
