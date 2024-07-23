package com.one.onekuji.repository;

import com.one.onekuji.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RoleRepository {
    Optional<Role> findByName(String name);
}
