package com.one.onekuji.repository;

import com.one.onekuji.model.StoreProduct;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StoreProductMapper {

    @Select("SELECT * FROM store_product")
    List<StoreProduct> findAll();

    @Select("SELECT * FROM store_product WHERE store_product_id = #{id}")
    StoreProduct findById(Long id);

    @Insert("INSERT INTO store_product (" +
            "product_name, description, price, stock_quantity, sold_quantity, image_url, category_id, status, created_at, updated_at, created_user, update_user, special_price, shipping_method, size, shipping_price) " +
            "VALUES (#{productName}, #{description}, #{price}, #{stockQuantity}, #{soldQuantity}, #{imageUrl}, #{categoryId}, #{status}, #{createdAt}, #{updatedAt}, #{createdUser}, #{updateUser}, #{specialPrice}, #{shippingMethod}, #{size}, #{shippingPrice})")
    @Options(useGeneratedKeys = true, keyProperty = "storeProductId")
    void insert(StoreProduct storeProduct);

    @Update("UPDATE store_product SET product_name=#{productName}, description=#{description}, price=#{price}, stock_quantity=#{stockQuantity}, sold_quantity=#{soldQuantity}, image_url=#{imageUrl}, category_id=#{categoryId}, " +
            "status=#{status}, updated_at=#{updatedAt}, special_price=#{specialPrice} , shipping_method =#{shippingMethod}, size =#{size}, shipping_price =#{shippingPrice} WHERE store_product_id=#{storeProductId}")
    void update(StoreProduct storeProduct);

    @Delete("DELETE FROM store_product WHERE store_product_id = #{id}")
    void delete(Long id);
}
