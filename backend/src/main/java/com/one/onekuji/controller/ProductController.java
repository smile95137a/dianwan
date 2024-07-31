package com.one.onekuji.controller;

import com.one.onekuji.model.Product;
import com.one.onekuji.request.ProductReq;
import com.one.onekuji.service.ProductService;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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

    @Operation(summary = "創建新的獎品", description = "創建一個新的獎品")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "獎品創建成功"),
            @ApiResponse(responseCode = "400", description = "無效的輸入")
    })
    @PostMapping("/add")
    public ResponseEntity<String> createPrize(
            @Parameter(description = "要創建的獎品詳細信息") @RequestBody ProductReq product ,
                                                         @RequestParam(value = "imageJson") String productJson,
                                                         @RequestParam(value = "image", required = false) MultipartFile file) {
        // 处理文件上传
        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            // 生成唯一的文件名
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            File uploadFile = new File(uploadDir + fileName);
            try {
                Files.createDirectories(Paths.get(uploadDir));
                file.transferTo(uploadFile);
                fileUrl = "/uploads/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("文件上傳失敗", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        product.setImageUrl(fileUrl);
        String isSuccess = productService.createProduct(product);
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
            @RequestParam(required = false) MultipartFile image,
            @RequestParam ProductReq productReq) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            if (image != null && !image.isEmpty()) {
                // 保存上传的图片并获取 URL
                String imageUrl = saveImage(image);
                productReq.setImageUrl(imageUrl); // 更新请求对象中的图片 URL
            }

            String isSuccess = productService.updateProduct(productId, productReq);
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
