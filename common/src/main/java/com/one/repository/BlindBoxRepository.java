package com.one.repository;

import com.one.model.BlindBox;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlindBoxRepository {

    @Select("select * from blindBox")
    List<BlindBox> getAllBlindBox();
    @Select("select * from blindBox where blind_box_id = #{blindBoxId}")
    BlindBox getBlindBoxById(Integer blindBoxId);
    @Insert("INSERT INTO blindbox (" +
            "blind_box_name, description, price, stock_quantity, sold_quantity, " +
            "image_url, rarity, created_at, start_date, end_date, created_user) " +
            "VALUES (#{blindBoxName}, #{description}, #{price}, #{stockQuantity}, #{soldQuantity}, " +
            "#{imageUrl}, #{rarity}, #{createdAt}, #{startDate}, #{endDate}, #{createdUser})")
    void createBlindBox(BlindBox blindBox);

    @Update("UPDATE blindbox SET " +
            "blind_box_name = #{blindBoxName}, " +
            "description = #{description}, " +
            "price = #{price}, " +
            "stock_quantity = #{stockQuantity}, " +
            "sold_quantity = #{soldQuantity}, " +
            "image_url = #{imageUrl}, " +
            "rarity = #{rarity}, " +
            "updated_at = #{updatedAt}, " +
            "start_date = #{startDate}, " +
            "end_date = #{endDate}, " +
            "update_user = #{updateUser} " +
            "WHERE blind_box_id = #{blindBoxId}")
    void updateBlindBox(BlindBox blindBox);


    @Delete("DELETE FROM blind_boxes WHERE blind_box_id = #{blindBoxId}")
    void deleteBlindBox(@Param("blindBoxId") Integer blindBoxId);

    void updateBlindBoxRemainingQuantity(BlindBox blindBox);
}
