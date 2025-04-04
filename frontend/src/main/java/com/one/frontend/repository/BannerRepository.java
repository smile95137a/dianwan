package com.one.frontend.repository;

import com.one.frontend.model.Banner;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BannerRepository {

    @Select("SELECT * FROM banner WHERE banner_id = #{bannerId}")
    Banner findById(@Param("bannerId") Long bannerId);

    @Select("SELECT * , b.product_id , b.image_urls , b.product_type FROM banner a left join product b on a.product_id = b.product_id")
    List<Banner> findAll();

}
