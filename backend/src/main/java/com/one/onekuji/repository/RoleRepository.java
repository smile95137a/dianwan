package com.one.onekuji.repository;

import com.one.onekuji.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RoleRepository {
    Optional<Role> findByName(String name);

    @Select("select * from role")
    List<Role> getAllRole();

    @Select("select * from role where id = #{roleId}")
    Role getRoleById(Integer roleId);
}
