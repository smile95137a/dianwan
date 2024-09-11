package com.one.frontend.repository;

import com.one.frontend.model.Banner;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BannerRepository {

    @Select("SELECT * FROM banner WHERE banner_id = #{bannerId}")
    Banner findById(@Param("bannerId") Long bannerId);

    @Select("SELECT * FROM banner")
    List<Banner> findAll();

}
