package com.one.repository;

import com.one.model.Gacha;
import com.one.model.GachaDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GachaDetailRepository {
    @Select("select * from gacha")
    List<GachaDetail> getAllGachaDetail();
    @Select("select * from gacha where gacha_detail_id = #{gachaId}")
    Gacha getGachaById(Long gachaId);
}
