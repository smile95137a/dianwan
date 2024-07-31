package com.one.onekuji.service;

import com.one.onekuji.model.Role;
import com.one.onekuji.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;


    public List<Role> getAllRole() {
        return repository.getAllRole();
    }

    public Role getRoleById(Integer roleId){
        return repository.getRoleById(roleId);
    }
}
