package com.one.onekuji.repository;

import com.one.onekuji.model.BlindBox;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlindBoxRepository {
    void updateBlindBox(BlindBox blindBox);
    @Select("select * from blindBox")
    List<BlindBox> getAllBlindBox();
    @Select("select * from blindBox where blindBoxId = #{blindBoxId}")
    BlindBox getBlindBoxById(Integer blindBoxId);

    void createBlindBox(BlindBox blindBox);

    void deleteBlindBox(Integer blindBoxId);
}
