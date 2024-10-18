package com.one.frontend.repository;

import com.one.frontend.model.Product;
import com.one.frontend.response.ProductRes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductRepository {

    @Select("SELECT p.*, pc.category_uuid, " +
            "SUM(CASE WHEN pd.grade <> 'LAST' THEN pd.quantity ELSE 0 END) as detailQuantity, " +
            "SUM(CASE WHEN pd.grade <> 'LAST' THEN pd.stock_quantity ELSE 0 END) as detailStockQuantity " +
            "FROM product p " +
            "LEFT JOIN product_detail pd ON p.product_id = pd.product_id " +
            "LEFT JOIN product_category pc ON p.category_id = pc.category_id " +
            "GROUP BY p.product_id " +
            "LIMIT #{size} OFFSET #{offset}")
	List<ProductRes> getAllProduct(@Param("offset") int offset, @Param("size") int size);



    @Select("SELECT * FROM product WHERE product_id = #{productId}")
    ProductRes getProductById(@Param("productId") Long productId);

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
            "rarity = #{rarity}, " +
            "updated_at = #{updatedAt}, " +
            "start_date = #{startDate}, " +
            "end_date = #{endDate}, " +
            "update_user = #{updateUser} " +
            "product_type = #{productType}" +
            "prize_category = #{prizeCategory}" +
            "status = #{status]" +
            "WHERE product_id = #{productId}")
    void updateProduct(Product product);

    @Delete("DELETE FROM product WHERE product_id = #{productId}")
    void deleteProduct(@Param("productId") Integer productId);

    @Update("UPDATE product SET " +
            "stock_quantity = #{stockQuantity} " +
            "WHERE product_id = #{productId}")
    void updateProductQuantity(ProductRes product);
    @Update("update product set status = 'SOLD_OUT' where product_id = #{productId}")
    void updateStatus(Long productId);
    @Select("select category_id from product_category where category_uuid = #{uuid}")
    Long getProductByCategoryId(String uuid);
    @Select("SELECT * FROM product WHERE category_id = #{categoryId}")
    ProductRes getProductByCId(Long categoryId);
}
