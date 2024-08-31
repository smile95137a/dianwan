package com.one.frontend.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.one.frontend.model.StoreProduct;
import com.one.frontend.response.StoreProductRes;

@Mapper
public interface StoreProductRepository {

	@Select("SELECT * " +
	        "FROM store_product " +
	        "LIMIT #{size} OFFSET #{offset}")
	List<StoreProductRes> findAll(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT * " +
            "FROM store_product WHERE store_product_id = #{id}")
    StoreProductRes findById(Long id);

    @Select("SELECT * FROM store_product WHERE store_product_id = #{storeProductId}")
    StoreProduct findId(@Param("storeProductId") Long storeProductId);
}
