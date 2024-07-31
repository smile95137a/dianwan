package com.one.onekuji.controller;

import com.one.onekuji.model.ProductDetail;
import com.one.onekuji.request.ProductDetailReq;
import com.one.onekuji.service.ProductDetailService;
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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Operation(summary = "创建新的奖品", description = "创建一个新的奖品")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "奖品创建成功"),
            @ApiResponse(responseCode = "400", description = "无效的输入")
    })
    @PostMapping("/add")
    public ResponseEntity<String> createPrize(
            @Parameter(description = "要创建的奖品详细信息") @RequestParam(required = false) MultipartFile image,
            @Parameter(description = "奖品详细信息") @RequestPart ProductDetailReq productDetailReq) {

        if (image != null && !image.isEmpty()) {
            // 保存上传的图片并获取 URL
            String imageUrl = saveImage(image);
            productDetailReq.setImage(imageUrl); // 更新请求对象中的图片 URL
        }

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
            @RequestParam(required = false) MultipartFile image,
            @RequestParam ProductDetailReq productReq) {

        ProductDetail product = productDetailService.getProductDetailById(productDetailId);
        if (product != null) {
            if (image != null && !image.isEmpty()) {
                // 保存上传的图片并获取 URL
                String imageUrl = saveImage(image);
                productReq.setImage(imageUrl); // 更新请求对象中的图片 URL
            }

            String isSuccess = productDetailService.updateProductDetail(productDetailId, productReq);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失败", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("奖品未找到", HttpStatus.NOT_FOUND);
        }
    }

    private String saveImage(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // 构造图片的 URL
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();
        } catch (Exception e) {
            throw new RuntimeException("无法保存文件: " + e.getMessage());
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
