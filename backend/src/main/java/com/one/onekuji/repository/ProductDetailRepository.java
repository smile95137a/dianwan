package com.one.onekuji.repository;

import com.one.onekuji.model.ProductDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductDetailRepository {

    @Select("select * from product_detail")
    List<ProductDetail> getAllProductDetail();
    @Select("select * from product_detail where product_detail_id = #{productDetailId}")
    ProductDetail getProductDetailById(Integer productDetailId);

    @Insert("INSERT INTO product_detail (" +
            "product_id, product_name, quantity , description, size, grade, image, create_date) " +
            "VALUES (#{productId}, #{productName} , #{quantity}, #{description}, #{size}, #{grade}, #{image}, #{createDate})")
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
    @Select("select * from product_detail where product_id = #{productId}")
    List<ProductDetail> getProductDetailByProductId(Long productId);


    @Update("UPDATE product_detail SET " +
            "quantity = #{quantity} " +
            "WHERE product_detail_id = #{productDetailId}")
    void updateProductDetailQuantity(ProductDetail productDetail);

    @Select("select product_name from product_detail where product_id = #{productId}")
    String getProductDetailByProduct(Long productId);
    @Delete("DELETE FROM product_detail WHERE product_id = #{productId}")
    void deleteProductDetailByProductId(Integer productId);
}