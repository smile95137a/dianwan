package com.one.frontend.repository;

import com.one.frontend.model.PrizeNumber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PrizeNumberMapper {

    @Select("select a.product_id ,  a.product_detail_id , a.prize_number_id , a.number , a.is_drawn , a.level from prize_number a join product_detail b on a.product_detail_id = b.product_detail_id\n" +
            "where a.product_detail_id = #{productDetailId}")
    List<PrizeNumber> getAllPrizeNumbersByProductDetailId(Long productDetailId);

    @Select("SELECT * FROM prize_number WHERE product_id = #{productId} AND is_drawn = FALSE")
    List<PrizeNumber> getAvailablePrizeNumbersByProductDetailId(Long productId);

    @Update("UPDATE prize_number SET is_drawn = TRUE WHERE prize_number_id = #{prizeNumberId} and product_id = #{productId} and product_detail_id = #{productDetailId}")
    void markPrizeNumberAsDrawn(Long prizeNumberId , Long productId , Long productDetailId);

    @Select("select `number` from prize_number where product_id = 6 and is_drawn = false;")
    List<Long> getNumbers(Long productId);
}