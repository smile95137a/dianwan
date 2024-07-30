package com.one.repository;

import com.one.model.ProductDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductDetailRepository {

    @Select("select * from product_detail")
    List<ProductDetail> getAllProductDetail();
    @Select("select * from product_detail where product_detail_id = #{productDetailId}")
    ProductDetail getProductDetailById(Integer productDetailId);

    @Insert("INSERT INTO product_detail (" +
            "product_id, product_name, description, size, grade, image, create_date) " +
            "VALUES (#{productId}, #{productName}, #{description}, #{size}, #{grade}, #{image}, #{createDate})")
    @Options(useGeneratedKeys = true, keyProperty = "productDetailId")
    void createProductDetail(ProductDetail productDetail);

    @Update("UPDATE product_detail SET " +
            "product_id = #{productId}, " +
            "product_name = #{productName}, " +
            "description = #{description}, " +
            "size = #{size}, " +
            "grade = #{grade}, " +
            "image = #{image}, " +
            "update_date = #{updateDate} " +
            "WHERE product_detail_id = #{productDetailId}")
    void updateProductDetail(ProductDetail productDetail);

    @Delete("DELETE FROM product_detail WHERE product_detail_id = #{productDetailId}")
    void deleteProductDetail(Integer productDetailId);
}