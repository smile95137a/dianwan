package com.one.frontend.repository;

import com.one.frontend.model.DrawResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrawRepository {

    @Insert("<script>" +
            "INSERT INTO drawresult (user_id, product_id, product_detail_id, draw_time, status, amount, draw_count, create_date, update_date, total_draw_count, remaining_draw_count) " +
            "VALUES " +
            "<foreach collection='drawResults' item='drawResult' index='index' separator=','>" +
            "(#{drawResult.userId}, #{drawResult.productId}, #{drawResult.productDetailId}, #{drawResult.drawTime}, #{drawResult.status}, #{drawResult.amount}, #{drawResult.drawCount}, #{drawResult.createDate}, #{drawResult.updateDate}, #{drawResult.totalDrawCount}, #{drawResult.remainingDrawCount})" +
            "</foreach>" +
            "</script>")
    void insertBatch(@Param("drawResults") List<DrawResult> drawResults);

    @Insert("INSERT INTO DrawResult (user_id, product_detail_id, draw_time, prize_number) VALUES (#{userId}, #{productDetailId}, #{drawTime}, #{prizeNumber})")
    void insertDrawResult(DrawResult drawResult);
}
