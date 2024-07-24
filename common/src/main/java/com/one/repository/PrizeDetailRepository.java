package com.one.repository;

import com.one.model.PrizeDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrizeDetailRepository {

    @Select("SELECT * FROM prize_detail")
    List<PrizeDetail> getAllPrizeDetails();

    @Select("SELECT * FROM prize_detail WHERE prize_detail_id = #{prizeDetailId}")
    PrizeDetail getPrizeDetailById(@Param("prizeDetailId") Integer prizeDetailId);

    @Insert("INSERT INTO prize_detail (description, rarity, size, material, is_secret, quantity, product_name, grade, count, create_date, update_date, image, status) " +
            "VALUES (#{description}, #{rarity}, #{size}, #{material}, #{isSecret}, #{quantity}, #{productName}, #{grade}, #{count}, #{createDate}, #{updateDate}, #{image}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "prizeId")
    void createPrizeDetail(PrizeDetail prizeDetail);

    @Update("UPDATE prize_detail SET description = #{description}, rarity = #{rarity}, size = #{size}, material = #{material}, is_secret = #{isSecret}, quantity = #{quantity}, " +
            "product_name = #{productName}, grade = #{grade}, count = #{count}, create_date = #{createDate}, update_date = #{updateDate}, image = #{image}, status = #{status} " +
            "WHERE prize_detail_id = #{prizeId}")
    void updatePrizeDetail(PrizeDetail prizeDetail);

    @Delete("DELETE FROM prize_detail WHERE prize_detail_id = #{prizeDetailId}")
    void deletePrizeDetail(@Param("prizeDetailId") Integer prizeDetailId);
}
