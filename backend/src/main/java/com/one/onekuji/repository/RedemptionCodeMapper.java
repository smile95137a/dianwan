package com.one.onekuji.repository;

import com.one.onekuji.model.RedemptionCode;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RedemptionCodeMapper {

    @Insert("INSERT INTO redemption_codes (code, is_redeemed, redeemed_at) VALUES (#{code}, #{isRedeemed}, #{redeemedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertRedemptionCode(RedemptionCode redemptionCode);

    @Select("SELECT * FROM redemption_codes WHERE code = #{code}")
    Optional<RedemptionCode> findByCode(String code);

    @Update("UPDATE redemption_codes SET is_redeemed = #{isRedeemed}, redeemed_at = #{redeemedAt}, user_id = #{userId} WHERE id = #{id}")
    void updateRedemptionCode(RedemptionCode redemptionCode);

    @Select("SELECT * FROM redemption_codes")
    List<RedemptionCode> findById();
}
