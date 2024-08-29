package com.one.onekuji.repository;

import com.one.onekuji.model.PrizeNumber;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrizeNumberMapper {

    @Insert({
            "<script>",
            "INSERT INTO prize_number (product_id, product_detail_id, number, is_drawn, level) VALUES ",
            "<foreach collection='prizeNumbers' item='item' separator=','>",
            "(#{item.productId}, #{item.productDetailId}, #{item.number}, #{item.isDrawn}, #{item.level})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "prizeNumberId")
    void insertBatch(@Param("prizeNumbers") List<PrizeNumber> prizeNumbers);
    @Select("SELECT MAX(number) FROM prizenumber WHERE product_id = #{productId}")
    Integer getMaxPrizeNumberByProductId(Long productId);

    @Delete("DELETE FROM prizenumber WHERE product_id = #{productId}")
    void deleteProductById(Integer productId);
    @Delete("DELETE FROM prizenumber WHERE product_detail_id = #{productDetailId}")
    void deteleByProductDetail(Integer productDetailId);
}
