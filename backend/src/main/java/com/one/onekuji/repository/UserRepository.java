package com.one.onekuji.repository;

import com.one.onekuji.model.User;
import com.one.onekuji.response.UserRes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRepository {

    @Select("SELECT id, username, password, nickname, email, phone_number, address, role_id, balance, bonus, sliver_coin, updated_at, draw_count FROM user")
    List<UserRes> findAll();

    @Select("SELECT id, username, password, nick_name, email, phone_number, address, role_id, balance, bonus, sliver_coin,, updated_at, draw_count FROM user WHERE id = #{userId}")
    UserRes findById(Long userId);

    // 插入新用戶
    @Insert("INSERT INTO user (username, password, nick_name, email, phone_number, address, role_id, balance, created_at) " +
            "VALUES (#{username}, #{password}, #{nickname}, #{email}, #{phoneNumber}, #{address}, #{roleId}, #{balance}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(User user);

    // 更新用戶
    @Update("UPDATE user SET username = #{username}, password = #{password}, nick_name = #{nickname}, email = #{email}, phone_number = #{phoneNumber}, address = #{address}, " +
            "role_id = #{roleId}, balance = #{balance}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    void update(User user);


    @Delete("DELETE FROM user WHERE id = #{userId}")
    void delete(Long userId);

    @Select("select * from user where username = #{username}")
    User getUserByUserName(String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById2(Long id);
}
