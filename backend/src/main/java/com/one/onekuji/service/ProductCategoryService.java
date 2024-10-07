package com.one.onekuji.service;

import com.one.onekuji.model.ProductCategory;
import com.one.onekuji.repository.ProductCategoryMapper;
import com.one.onekuji.repository.ProductRepository;
import com.one.onekuji.response.ProductRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    // 查詢所有類別
    public List<ProductCategory> getAllCategories() {
        return productCategoryMapper.getAllCategories();
    }

    // 根據ID查詢類別
    public ProductCategory getCategoryById(Long categoryId) {
        return productCategoryMapper.getCategoryById(categoryId);
    }

    // 創建新類別
    public ProductCategory createCategory(ProductCategory category) {
        category.setCategoryUUid(UUID.randomUUID().toString());
        productCategoryMapper.createCategory(category);
        return category;
    }

    // 更新類別
    public ProductCategory updateCategory(Long categoryId, ProductCategory category) {
        ProductCategory existingCategory = productCategoryMapper.getCategoryById(categoryId);
        if (existingCategory != null) {
            category.setCategoryId(categoryId); // 設置 ID 保持一致
            category.setCategoryUUid(existingCategory.getCategoryUUid());
            productCategoryMapper.updateCategory(category);
            return category;
        }
        return null;
    }

    // 刪除類別
    public boolean deleteCategory(Long categoryId) {
        ProductCategory existingCategory = productCategoryMapper.getCategoryById(categoryId);
        ProductRes res =  productRepository.getProductByCategoryId(categoryId);
        if(res != null){
            productService.deleteProduct(Long.valueOf(res.getProductId()));
        }
        if (existingCategory != null) {

            productCategoryMapper.deleteCategory(categoryId);
                return true;
        }
        return false;
    }
}
