package com.one.onekuji.repository;

import com.one.onekuji.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRepository{
    @Select("select * from user where id = ${userId}")
    User getUserById(Integer userId);

    @Select("SELECT * FROM user")
    List<User> getAllUser();


    void createUser(User user);

    void update(User user);

    void deleteUser(Integer userId);
}