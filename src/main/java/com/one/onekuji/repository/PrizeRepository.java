package com.one.onekuji.repository;

import com.one.onekuji.model.Prize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PrizeRepository {
    @Select("select * from prize")
    List<Prize> getAllPrize();
    @Select("select * from prize where prizeId = #{prizeId}")
    Prize getPrizeById(Integer prizeId);

    void updatePrize(Prize prize);

    void createPrize(Prize prize);

    void deletePrize(Integer prizeId);
}
