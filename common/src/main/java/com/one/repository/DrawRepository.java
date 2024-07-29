package com.one.repository;

import com.one.model.DrawResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DrawRepository {

    @Select("SELECT * FROM drawresult WHERE id = #{id}")
    DrawResult findById(Long id);

    @Select("SELECT * FROM drawresult WHERE user_id = #{userId}")
    List<DrawResult> findByUserId(Integer userId);

    @Insert("INSERT INTO drawresult (user_id, blind_box_id, prize_detail_id, gacha_id, draw_time, status, amount, draw_count) " +
            "VALUES (#{userId}, #{blindBoxId}, #{prizeDetailId}, #{gachaId}, #{drawTime}, #{status}, #{amount}, #{drawCount})")
    void insert(DrawResult drawResult);
    @Insert("<script>" +
            "INSERT INTO drawresult (user_id, blind_box_id, prize_detail_id, gacha_id, draw_time, status, amount, draw_count, create_date, update_date) " +
            "VALUES " +
            "<foreach collection='drawResults' item='drawResult' separator=','>" +
            "(#{drawResult.userId}, #{drawResult.blindBoxId}, #{drawResult.prizeDetailId}, #{drawResult.gachaId}, #{drawResult.drawTime}, #{drawResult.status}, #{drawResult.amount}, #{drawResult.drawCount}, #{drawResult.createDate}, #{drawResult.updateDate})" +
            "</foreach>" +
            "</script>")
    void insertBatch(List<DrawResult> drawResults);

}
