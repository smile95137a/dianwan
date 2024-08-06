package com.one.onekuji.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.onekuji.eenum.ProductType;
import com.one.onekuji.model.Product;
import com.one.onekuji.request.ProductReq;
import com.one.onekuji.service.ProductService;
import com.one.onekuji.util.ImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Operation(summary = "獲取所有獎品", description = "檢索所有獎品的列表")
    @GetMapping("/query")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取獎品", description = "根據其 ID 獲取獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品檢索成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer productId) {
        Product prize = productService.getProductById(productId);
        if (prize != null) {
            return new ResponseEntity<>(prize, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "獲取所有獎品", description = "檢索所有獎品的列表")
    @PostMapping("/type")
    public ResponseEntity<List<Product>> getAllProduct(@RequestBody ProductType type) {
        List<Product> products = productService.getAllProductByType(type);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @Operation(summary = "創建新的獎品", description = "創建一個新的獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "獎品創建成功"),
            @ApiResponse(responseCode = "400", description = "無效的輸入")
    })
    @PostMapping("/add")
    public ResponseEntity<String> createPrize(
            @Parameter(description = "要創建的獎品詳細信息") @RequestPart("productReq") String productReqJson,
            @Parameter(description = "獎品圖片") @RequestPart(value = "image", required = false) MultipartFile image) throws JsonProcessingException {

        ProductReq productDetailReq = new ObjectMapper().readValue(productReqJson, ProductReq.class);

        // 處理文件上傳
        String fileUrl = null;
        fileUrl = ImageUtil.upload(image);

        productDetailReq.setImageUrl(fileUrl);
        String isSuccess = productService.createProduct(productDetailReq);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.BAD_REQUEST);
        }
    }

    private Product convertJsonToProduct(String productJson) {
        // 实现 JSON 转换为 Product 对象的方法
        // 可以使用 Jackson 或 Gson 等库
        return new Product(); // Placeholder
    }

    @Operation(summary = "更新現有獎品", description = "根據 ID 更新現有的獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "獎品更新成功"),
            @ApiResponse(responseCode = "404", description = "獎品未找到")
    })
    @PutMapping("/{productId}")
    public ResponseEntity<String> updatePrize(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer productId,
            @Parameter(description = "獎品圖片") @RequestPart(value = "image", required = false) MultipartFile image,
            @Parameter(description = "獎品詳細信息") @RequestPart("productReq") String productReqJson) throws JsonProcessingException {

        ProductReq productDetailReq = new ObjectMapper().readValue(productReqJson, ProductReq.class);

        Product product = productService.getProductById(productId);
        if (product != null) {
            // 處理文件上傳
            String fileUrl = null;
            fileUrl = ImageUtil.upload(image);

            productDetailReq.setImageUrl(fileUrl);

            String isSuccess = productService.updateProduct(productId, productDetailReq);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失败", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("獎品未找到", HttpStatus.NOT_FOUND);
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
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(
            @Parameter(description = "獎品的 ID", example = "1") @PathVariable Integer productId) {
        String isSuccess = productService.deleteProduct(productId);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("刪除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.BAD_REQUEST);
        }
    }
}
