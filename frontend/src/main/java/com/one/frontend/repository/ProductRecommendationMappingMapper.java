package com.one.frontend.repository;

import com.one.frontend.response.RecommendRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductRecommendationMappingMapper {

    @Select("SELECT b.image_urls , b.product_name FROM product_recommendation_mapping a left join store_product b on a.store_product_id = b.store_product_id")
    List<RecommendRes> getAllMappings();

    @Select("SELECT c.id , c.recommendation_name , b.store_product_id , b.image_urls , b.product_name FROM product_recommendation_mapping a left join store_product b on a.store_product_id = b.store_product_id left join store_product_recommendation c on a.store_product_recommendation_id = c.id where c.id = #{id}")
    RecommendRes getMappingById(Long id);

}
