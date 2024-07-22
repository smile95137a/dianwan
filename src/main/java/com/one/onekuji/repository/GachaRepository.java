package com.one.onekuji.repository;

import com.one.onekuji.model.Gacha;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GachaRepository {
    @Select("select * from gacha")
    List<Gacha> getAllGacha();
    @Select("select * from gacha where gachaId = #{gachaId}")
    Gacha getGachaById(Integer gachaId);

    void createGacha(Gacha gacha);

    void updateGacha(Gacha gacha);

    void deleteBlindBox(Integer gachaId);
}
