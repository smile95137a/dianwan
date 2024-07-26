package com.one.repository;

import com.one.model.Prize;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrizeRepository {
    @Select("select * from prize")
    List<Prize> getAllPrize();
    @Select("select * from prize where prizeId = #{prizeId}")
    Prize getPrizeById(@Param("prizeId") Integer prizeId);
    @Update("UPDATE prizes SET price = #{price}, total_quantity = #{totalQuantity}, " +
            "remaining_quantity = #{remainingQuantity}, main_image_url = #{mainImageUrl}, " +
            "release_date = #{releaseDate}, manufacturer = #{manufacturer}, category = #{category}, " +
            "is_limited = #{isLimited}, is_active = #{isActive}, name = #{name}, " +
            "prize_details = #{prizeDetails}, create_date = #{createDate}, update_date = #{updateDate}, " +
            "start_date = #{startDate}, end_date = #{endDate}, status = #{status}, description = #{description}, " +
            "created_user = #{createdUser}, update_user = #{updateUser} " +
            "WHERE prize_id = #{prizeId}")
    void updatePrize(Prize prize);
    @Insert("INSERT INTO prizes (price, total_quantity, remaining_quantity, main_image_url, release_date, " +
            "manufacturer, category, is_limited, is_active, name, prize_details, create_date, update_date, " +
            "start_date, end_date, status, description, created_user, update_user) " +
            "VALUES (#{price}, #{totalQuantity}, #{remainingQuantity}, #{mainImageUrl}, #{releaseDate}, " +
            "#{manufacturer}, #{category}, #{isLimited}, #{isActive}, #{name}, #{prizeDetails}, " +
            "#{createDate}, #{updateDate}, #{startDate}, #{endDate}, #{status}, #{description}, " +
            "#{createdUser}, #{updateUser})")
    void createPrize(Prize prize);
    @Delete("DELETE FROM prizes WHERE prize_id = #{prizeId}")
    void deletePrize(@Param("prizeId") Integer prizeId);

    @Update("UPDATE prize SET remaining_quantity = #{remainingQuantity} WHERE prize_id = #{prizeId}")
    void updatePrizeRemainingQuantity(Prize prize);
}
