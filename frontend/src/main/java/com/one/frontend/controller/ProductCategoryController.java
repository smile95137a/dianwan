package com.one.frontend.controller;

import com.one.frontend.model.ApiResponse;
import com.one.frontend.model.ProductCategory;
import com.one.frontend.service.ProductCategoryService;
import com.one.frontend.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productCategory")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    // 查詢所有類別
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllCategories() {
        List<ProductCategory> categories = productCategoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.failure(404, "無類別", null));
        }
        return ResponseEntity.ok(ResponseUtils.success(200, null, categories));
    }

    // 根據ID查詢類別
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<ProductCategory>> getCategoryById(@PathVariable("uuid") String uuid) {
        ProductCategory category = productCategoryService.getCategoryById(uuid);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.failure(404, "類別未找到", null));
        }
        return ResponseEntity.ok(ResponseUtils.success(200, null, category));
    }


}
