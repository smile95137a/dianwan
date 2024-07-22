package com.one.onekuji.repository;

import com.one.onekuji.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRepository{
    @Select("select * from user where id = ${userId}")
    User getUserById(@Param("userId") Integer userId);

    @Select("SELECT * FROM user")
    List<User> getAllUser();


    @Insert("INSERT INTO user (username, password, nickname, email, phone_number, address, created_at, updated_at) " +
            "VALUES (#{username}, #{password}, #{nickname}, #{email}, #{phoneNumber}, #{address}, #{createdAt}, #{updatedAt})")
    void createUser(User user);
    @Update("UPDATE user SET username = #{username}, password = #{password}, nickname = #{nickname}, " +
            "email = #{email}, phone_number = #{phoneNumber}, address = #{address}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    void update(User user);
    @Delete("DELETE FROM user WHERE id = #{userId}")
    void deleteUser(@Param("userId") Integer userId);
}