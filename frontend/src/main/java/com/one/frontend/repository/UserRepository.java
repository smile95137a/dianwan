package com.one.frontend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.one.frontend.model.DrawResult;
import com.one.frontend.model.Role;
import com.one.frontend.model.User;
import com.one.frontend.response.UserRes;

@Mapper
public interface UserRepository{
    @Select("select * from user where id = ${userId}")
    UserRes getUserById(@Param("userId") Integer userId);

    @Select("select * from user where id = ${userId}")
    User getUserBId(@Param("userId") Integer userId);

    @Select("SELECT * FROM user")
    List<User> getAllUser();


    @Insert("INSERT INTO `user` (username, password, email, phone_number, address, created_at , balance , bonus, provider ) " +
            "VALUES (#{username}, #{password}, #{email}, #{phoneNumber}, #{address}, #{createdAt} , #{balance} , #{bonus}, #{provider})")
    void createUser(User user);

    @Update("UPDATE `user` SET password = #{password}, nickname = #{nickName}, " +
            "email = #{email}, phone_number = #{phoneNumber}, address = #{address}, updated_at = #{updatedAt} " +
            "WHERE id = #{id}")
    void update(UserRes user);

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
    void deductUserBalance(@Param("userId") Integer userId, @Param("amount") BigDecimal amount);


    void insertBatch(List<DrawResult> drawResults);

    @Select("select balance from user where id = #{userId}")
    Integer getBalance(Integer userId);
    @Select("select * from user where username = #{username}")
    User getUserByUserName(String username);
    @Select("select bonus from `user` where id = #{userId}")
    Integer getBonusPoints(Integer userId);

    void deductUserBonusPoints(Integer userId, BigDecimal newBonusPoints);
    @Update("update `user` set balance = balance + tradeAmt where id = #{userId}")
    void updateAccoutnt(String tradeAmt , String userId);
    @Select("select * from `user` where google_id = #{googleId}")
    User findByGoogleId(String googleId);

    @Insert("INSERT INTO `user` (username, password, email, phone_number, address, createdAt, balance, bonus, google_id) " +
            "VALUES (#{username}, #{password}, #{email}, #{phoneNumber}, #{address}, #{createdAt}, #{balance}, #{bonus}, #{googleId})")
    void createGoogleUser(User user);


    @Update("update `user` set bonus = bonus + 1 , draw_count = 0 where id = #{userId}")
    void updateBonus(Integer userId);

    @Update("update `user` set  draw_count = draw_count + 1 where id = #{userId} ")
    void addDrawCount();
    
    @Select("select * from user where email  = #{email}")
    Optional<User> getUserByEmail(String email);
}