package com.one.repository;

import com.one.model.BlindBox;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlindBoxRepository {

    @Select("select * from blindBox")
    List<BlindBox> getAllBlindBox();
    @Select("select * from blindBox where blindBoxId = #{blindBoxId}")
    BlindBox getBlindBoxById(Integer blindBoxId);
    @Insert("INSERT INTO blind_boxes (blind_box_name, description, price, stock_quantity, sold_quantity, " +
            "image_url, item_list, release_date, is_limited, manufacturer, category, rarity, is_active, " +
            "created_at, updated_at, start_date, end_date, created_user, update_user) " +
            "VALUES (#{blindBoxName}, #{description}, #{price}, #{stockQuantity}, #{soldQuantity}, " +
            "#{imageUrl}, #{itemList, jdbcType=VARCHAR}, #{releaseDate}, #{isLimited}, #{manufacturer}, #{category}, " +
            "#{rarity}, #{isActive}, #{createdAt}, #{updatedAt}, #{startDate}, #{endDate}, #{createdUser}, #{updateUser})")
    void createBlindBox(BlindBox blindBox);

    @Update("UPDATE blind_boxes SET blind_box_name = #{blindBoxName}, description = #{description}, price = #{price}, " +
            "stock_quantity = #{stockQuantity}, sold_quantity = #{soldQuantity}, image_url = #{imageUrl}, " +
            "item_list = #{itemList, jdbcType=VARCHAR}, release_date = #{releaseDate}, is_limited = #{isLimited}, " +
            "manufacturer = #{manufacturer}, category = #{category}, rarity = #{rarity}, is_active = #{isActive}, " +
            "created_at = #{createdAt}, updated_at = #{updatedAt}, start_date = #{startDate}, end_date = #{endDate}, " +
            "created_user = #{createdUser}, update_user = #{updateUser} " +
            "WHERE blind_box_id = #{blindBoxId}")
    void updateBlindBox(BlindBox blindBox);

    @Delete("DELETE FROM blind_boxes WHERE blind_box_id = #{blindBoxId}")
    void deleteBlindBox(@Param("blindBoxId") Integer blindBoxId);

    void updateBlindBoxRemainingQuantity(BlindBox blindBox);
}
