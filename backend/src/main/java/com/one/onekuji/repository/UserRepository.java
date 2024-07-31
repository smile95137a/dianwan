package com.one.onekuji.repository;

import com.one.onekuji.model.DrawResult;
import com.one.onekuji.model.Role;
import com.one.onekuji.model.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface UserRepository{
    @Select("select * from user where id = ${userId}")
    User getUserById(@Param("userId") Integer userId);

    @Select("SELECT * FROM user")
    List<User> getAllUser();


    @Insert("INSERT INTO `user` (username, password, nickname, email, phoneNumber, address, createdAt , balance , bonus) " +
            "VALUES (#{username}, #{password}, #{nickname}, #{email}, #{phoneNumber}, #{address}, #{createdAt} , #{balance} , #{bonus})")
    void createUser(User user);

    @Update("UPDATE `user` SET password = #{password}, nickname = #{nickname}, " +
            "email = #{email}, phoneNumber = #{phoneNumber}, address = #{address}, updatedAt = #{updatedAt} " +
            "WHERE id = #{id}")
    void update(User user);

    @Delete("DELETE FROM user WHERE id = #{userId}")
    void deleteUser(@Param("userId") Integer userId);
    @Select("SELECT username, password , role_id FROM user WHERE username = #{username}")
    @Results({
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "selectRolesByUserId"))
    })
    Optional<User> findByUsername(@Param("username") String username);

    @Select("SELECT r.* FROM roles r " +
            "JOIN users_roles ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name")
    })
    Set<Role> selectRolesByUserId(Long userId);

    @Select("select * from user where role_id = #{roleId}")
    int countByRoleId(int roleId);
    @Update("UPDATE user SET balance = balance - #{amount} WHERE id = #{userId}")
    void deductUserBalance(Integer userId, BigDecimal amount);

    void insertBatch(List<DrawResult> drawResults);

    @Select("select balance from user where id = #{userId}")
    Integer getBalance(Integer userId);
    @Select("select * from user where username = #{username}")
    User getUserByUserName(String username);

    Integer getBonusPoints(Integer userId);

    void deductUserBonusPoints(Integer userId, BigDecimal newBonusPoints);
}