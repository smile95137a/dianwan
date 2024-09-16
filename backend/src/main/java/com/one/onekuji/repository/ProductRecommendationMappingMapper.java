package com.one.onekuji.repository;

import com.one.onekuji.model.ProductRecommendationMapping;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductRecommendationMappingMapper {

    @Select("SELECT * FROM product_recommendation_mapping")
    List<ProductRecommendationMapping> getAllMappings();

    @Select("SELECT * FROM product_recommendation_mapping WHERE id = #{id}")
    ProductRecommendationMapping getMappingById(Long id);

    @Insert("INSERT INTO product_recommendation_mapping(store_product_id, recommendation_id, created_date, updated_date, created_user, update_user) " +
            "VALUES (#{storeProductId}, #{storeProductRecommendationId}, #{createdDate}, #{updatedDate}, #{createdUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createMapping(ProductRecommendationMapping mapping);

    @Update("UPDATE product_recommendation_mapping SET store_product_id = #{storeProductId}, store_product_recommendation_id = #{storeProductRecommendationId}, " +
            "updated_date = #{updatedDate}, update_user = #{updateUser} WHERE id = #{id}")
    int updateMapping(ProductRecommendationMapping mapping);

    @Delete("DELETE FROM product_recommendation_mapping WHERE id = #{id}")
    int deleteMapping(Long id);
}
