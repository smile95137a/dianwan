package com.one.repository;

import com.one.model.Gacha;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GachaRepository {
    @Select("select * from gacha")
    List<Gacha> getAllGacha();
    @Select("select * from gacha where gacha_id = #{gachaId}")
    Gacha getGachaById(Integer gachaId);
    @Insert("INSERT INTO gachas (gacha_name, description, price, stock_quantity, sold_quantity, " +
            "image_url, item_list, release_date, is_limited, manufacturer, category, rarity, is_active, " +
            "created_at, updated_at, start_date, end_date, created_user, update_user) " +
            "VALUES (#{gachaName}, #{description}, #{price}, #{stockQuantity}, #{soldQuantity}, " +
            "#{imageUrl}, #{itemList}, #{releaseDate}, #{isLimited}, #{manufacturer}, #{category}, " +
            "#{rarity}, #{isActive}, #{createdAt}, #{updatedAt}, #{startDate}, #{endDate}, #{createdUser}, #{updateUser})")
    void createGacha(Gacha gacha);
    @Update("UPDATE gachas SET gacha_name = #{gachaName}, description = #{description}, price = #{price}, " +
            "stock_quantity = #{stockQuantity}, sold_quantity = #{soldQuantity}, image_url = #{imageUrl}, " +
            "item_list = #{itemList}, release_date = #{releaseDate}, is_limited = #{isLimited}, " +
            "manufacturer = #{manufacturer}, category = #{category}, rarity = #{rarity}, is_active = #{isActive}, " +
            "created_at = #{createdAt}, updated_at = #{updatedAt}, start_date = #{startDate}, end_date = #{endDate}, " +
            "created_user = #{createdUser}, update_user = #{updateUser} " +
            "WHERE gacha_id = #{gachaId}")
    void updateGacha(Gacha gacha);

    @Delete("DELETE FROM gachas WHERE gacha_id = #{gachaId}")
    void deleteGacha(@Param("gachaId") Integer gachaId);

    void updateGachaSoldQuantity(Gacha gacha1);
}
