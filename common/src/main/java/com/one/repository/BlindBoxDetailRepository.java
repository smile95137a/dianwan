package com.one.repository;

import com.one.model.BlindBoxDetail;
import com.one.model.Gacha;
import com.one.model.GachaDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlindBoxDetailRepository {
    @Select("select * from blind_box_detail")
    List<BlindBoxDetail> getAllBlindBoxDetail();
    @Select("select * from blind_box_detail where blind_box_detail_id = #{blindBoxId}")
    BlindBoxDetail getBlindBoxById(Long blindBoxId);

    @Select("select * from blind_box_detail where blind_box_id = #{blindBoxId}")
    List<BlindBoxDetail> getAllGachaDetailBygachaId(Long blindBoxId);

    void updateblindBoxDetailQuantity(BlindBoxDetail selectedBlindBoxDetail);
}
