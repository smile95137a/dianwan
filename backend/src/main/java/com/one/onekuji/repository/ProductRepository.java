package com.one.onekuji.repository;

import com.one.onekuji.eenum.ProductType;
import com.one.onekuji.model.Product;
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
            "image_url, rarity, created_at, start_date, end_date, created_user , product_type , prize_category , status) " +
            "VALUES (#{productName}, #{description}, #{price}, #{stockQuantity}, #{soldQuantity}, " +
            "#{imageUrl}, #{rarity}, #{createdAt}, #{startDate}, #{endDate}, #{createdUser} , #{productType} , #{prizeCategory} , #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "productId")
    void createProduct(Product product);


    @Update("UPDATE product SET " +
            "product_name = #{productName}, " +
            "description = #{description}, " +
            "price = #{price}, " +
            "stock_quantity = #{stockQuantity}, " +
            "sold_quantity = #{soldQuantity}, " +
            "image_url = #{imageUrl}, " +
//            "rarity = #{rarity}, " +
            "updated_at = #{updatedAt}, " +
            "start_date = #{startDate}, " +
            "end_date = #{endDate}, " +
//            "update_user = #{updateUser} " +
            "product_type = #{productType}" +
            "prize_category = #{prizeCategory}" +
            "status = #{status]" +
            "WHERE product_id = #{productId}")
    void updateProduct(Product product);

    @Delete("DELETE FROM product WHERE product_id = #{productId}")
    void deleteProduct(@Param("productId") Integer productId);

    @Update("UPDATE product SET " +
            "stock_quantity = #{stockQuantity}, " +
            "sold_quantity = #{soldQuantity} " +
            "WHERE product_id = #{productId}")
    void updateProductQuantity(Product product);

    @Select("select * from product where product_type = #{productType}")
    List<Product> getAllProductByType(ProductType productType);

    @Update("UPDATE `product` SET `image_url` = #{base64} WHERE `product_id` IN (1,2,3,4,5,6,7,8,9,10,11,12,13)")
    void updateImageUrlForProducts(@Param("base64") String base64);
}
