package com.one.frontend.repository;

import com.one.frontend.model.StoreProduct;
import com.one.frontend.response.StoreProductRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StoreProductRepository {

    @Select("SELECT product_name, description, price, stock_quantity, image_url, category_id, status, special_price, shipping_method, size, shipping_price " +
            "FROM store_product")
    List<StoreProductRes> findAll();

    @Select("SELECT product_name, description, price, stock_quantity, image_url, category_id, status, special_price, shipping_method, size, shipping_price " +
            "FROM store_product WHERE store_product_id = #{id}")
    StoreProductRes findById(Long id);

    @Select("SELECT * FROM store_product WHERE store_product_id = #{storeProductId}")
    StoreProduct findId(@Param("storeProductId") Long storeProductId);
}
