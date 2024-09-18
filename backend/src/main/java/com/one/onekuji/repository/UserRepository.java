package com.one.onekuji.repository;

import com.one.onekuji.model.User;
import com.one.onekuji.request.UserReq;
import com.one.onekuji.response.UserRes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRepository {

    @Select("SELECT id, username, password, nick_name, email, phone_number, address, role_id, balance, bonus, sliver_coin, updated_at, draw_count FROM user")
    List<UserRes> findAll();

    @Select("SELECT id, username, password, nick_name, email, phone_number, address, role_id, balance, bonus, sliver_coin,, updated_at, draw_count FROM user WHERE id = #{userId}")
    UserRes findById(Long userId);

    @Insert("INSERT INTO user (username, password, nick_name, email, phone_number, address, role_id, balance, bonus, sliver_coin, google_id, created_at, draw_count , role_id) " +
            "VALUES (#{username}, #{password}, #{nickName}, #{email}, #{phoneNumber}, #{address}, #{roleId}, #{balance}, #{bonus}, #{sliverCoin}, #{googleId}, #{createdAt}, #{drawCount}) , #{roleId}")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "id")
    void insert(UserReq userReq);

    @Update("UPDATE user SET username = #{username}, password = #{password}, nick_name = #{nickName}, email = #{email}, phone_number = #{phoneNumber}, address = #{address}, " +
            "role_id = #{roleId}, balance = #{balance}, bonus = #{bonus}, sliver_coin = #{sliverCoin}, google_id = #{googleId}, updated_at = NOW(), draw_count = #{drawCount} " +
            "WHERE id = #{userId}")
    void update(UserReq userReq);

    @Delete("DELETE FROM user WHERE id = #{userId}")
    void delete(Long userId);

    @Select("select * from user where username = #{username}")
    User getUserByUserName(String username);
}
