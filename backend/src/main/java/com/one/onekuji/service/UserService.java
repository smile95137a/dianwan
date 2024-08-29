package com.one.onekuji.service;

import com.one.onekuji.model.Role;
import com.one.onekuji.repository.RoleRepository;
import com.one.onekuji.request.UserReq;
import com.one.onekuji.response.UserRes;
import com.one.onekuji.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<UserRes> getAllUsers() {
        return userRepository.findAll();
    }

    public UserRes getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public UserRes createUser(UserReq userReq) {
        userReq.setCreatedAt(LocalDateTime.now());
        Role role = roleRepository.findByName("一般管理者");
        userReq.setRoleId(role.getId());

        userRepository.insert(userReq);
        return userRepository.findById(userReq.getUserId());
    }

    public UserRes updateUser(Long userId, UserReq userReq) {
        userReq.setUserId(userId);
        userReq.setUpdatedAt(LocalDateTime.now());
        userRepository.update(userReq);
        return userRepository.findById(userId);
    }

    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }
}
