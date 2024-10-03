package com.one.onekuji.repository;

import com.one.onekuji.model.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductCategoryMapper {

    // 查詢所有類別
    @Select("SELECT * FROM product_category")
    List<ProductCategory> getAllCategories();

    // 根據ID查詢類別
    @Select("SELECT * FROM product_category WHERE category_id = #{categoryId}")
    ProductCategory getCategoryById(Long categoryId);

    // 創建新類別
    @Insert("INSERT INTO product_category (category_name, category_UUid) VALUES (#{categoryName}, #{categoryUUid})")
    @Options(useGeneratedKeys = true, keyProperty = "categoryId")
    void createCategory(ProductCategory category);

    // 更新類別
    @Update("UPDATE product_category SET category_name = #{categoryName}, category_UUid = #{categoryUUid} WHERE category_id = #{categoryId}")
    void updateCategory(ProductCategory category);

    // 刪除類別
    @Delete("DELETE FROM product_category WHERE category_id = #{categoryId}")
    void deleteCategory(Long categoryId);
}
