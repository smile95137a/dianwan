package com.one.frontend.repository;

import com.one.frontend.model.PrizeNumber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PrizeNumberMapper {

    // 获取指定产品明细的所有奖品编号
    @Select("select a.number , a.is_drawn , b.grade from prizenumber a join product_detail b on a.product_detail_id = b.product_detail_id\n" +
            "where a.product_detail_id = #{productDetailId}")
    List<PrizeNumber> getAllPrizeNumbersByProductDetailId(Long productDetailId);

    // 获取指定产品明细中未被抽走的奖品编号
    @Select("SELECT * FROM prizenumber WHERE product_id = #{productDetailId} AND is_drawn = FALSE")
    List<PrizeNumber> getAvailablePrizeNumbersByProductDetailId(Long productDetailId);

    // 标记奖品编号为已抽走
    @Update("UPDATE prizenumber SET is_drawn = TRUE WHERE prize_number_id = #{prizeNumberId} and product_id = #{productId} and product_detail_id = #{productDetailId}")
    void markPrizeNumberAsDrawn(Long prizeNumberId , Long productId , Long productDetailId);
}