package com.one.repository;

import com.one.model.Gacha;
import com.one.model.GachaDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlindBoxDetailRepository {
    @Select("select * from blind_box_detail")
    List<GachaDetail> getAllBlindBoxDetail();
    @Select("select * from blind_box_detail where blind_box_detail_id = #{gachaId}")
    Gacha getBlindBoxById(Long gachaId);
}
