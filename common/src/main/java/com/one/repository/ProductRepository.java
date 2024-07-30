package com.one.repository;

import com.one.model.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductRepository {

    @Select("SELECT * FROM product")
    List<Product> getAllProduct();

    @Select("SELECT * FROM product WHERE product_id = #{productId}")
    Product getProductById(@Param("productId") Integer productId);

    @Insert("INSERT INTO product (" +
            "product_name, description, price, stock_quantity, sold_quantity, " +
            "image_url, rarity, created_at, start_date, end_date, created_user) " +
            "VALUES (#{productName}, #{description}, #{price}, #{stockQuantity}, #{soldQuantity}, " +
            "#{imageUrl}, #{rarity}, #{createdAt}, #{startDate}, #{endDate}, #{createdUser})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void createProduct(Product product);


    @Update("UPDATE product SET " +
            "product_name = #{productName}, " +
            "description = #{description}, " +
            "price = #{price}, " +
            "stock_quantity = #{stockQuantity}, " +
            "sold_quantity = #{soldQuantity}, " +
            "image_url = #{imageUrl}, " +
            "rarity = #{rarity}, " +
            "updated_at = #{updatedAt}, " +
            "start_date = #{startDate}, " +
            "end_date = #{endDate}, " +
            "update_user = #{updateUser} " +
            "WHERE product_id = #{productId}")
    void updateProduct(Product product);

    @Delete("DELETE FROM product WHERE product_id = #{productId}")
    void deleteProduct(@Param("productId") Integer productId);
}
