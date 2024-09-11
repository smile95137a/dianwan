package com.one.onekuji.repository;

import com.one.onekuji.model.Banner;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BannerRepository {

    @Select("SELECT * FROM banner WHERE banner_id = #{bannerId}")
    Banner findById(@Param("bannerId") Long bannerId);

    @Select("SELECT * FROM banner")
    List<Banner> findAll();

    @Insert("INSERT INTO banner (banner_uid, banner_image_url, product_id, status, created_at, updated_at) " +
            "VALUES (#{bannerUid}, #{bannerImageUrl}, #{productId}, #{status}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "bannerId")
    void insert(Banner banner);

    @Update("UPDATE banner SET banner_image_url=#{bannerImageUrl}, product_id=#{productId}, " +
            "status=#{status}, updated_at=#{updatedAt} WHERE banner_id=#{bannerId}")
    void update(Banner banner);

    @Delete("DELETE FROM banner WHERE banner_id = #{bannerId}")
    void deleteById(@Param("bannerId") Long bannerId);
}
