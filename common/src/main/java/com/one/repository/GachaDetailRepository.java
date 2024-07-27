package com.one.repository;

import com.one.model.Gacha;
import com.one.model.GachaDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GachaDetailRepository {
    @Select("select * from gacha_detail")
    List<GachaDetail> getAllGachaDetail();
    @Select("select * from gacha_detail where gacha_detail_id = #{gachaDetailId}")
    Gacha getGachaById(Long gachaDetailId);

    @Select("select * from gacha_detail where gacha_id = #{gachaId}")
    List<GachaDetail> getAllGachaDetailBygachaId(Long gachaId);

    void updateGachaDetailQuantity(GachaDetail selectedPrizeDetail);
}
