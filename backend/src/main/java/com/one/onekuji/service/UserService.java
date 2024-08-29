package com.one.onekuji.service;

import com.one.onekuji.repository.RoleRepository;
import com.one.onekuji.repository.UserRepository;
import com.one.onekuji.request.UserReq;
import com.one.onekuji.response.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // 獲取所有用戶
    public List<UserRes> getAllUsers() {
        return userRepository.getAllUser();
    }

    // 根據ID獲取用戶
    public UserRes getUserById(Long id) {
        return userRepository.getUserById(Math.toIntExact(id));
    }

    // 創建新用戶
    public void createUser(UserReq userReq) {
        userRepository.createUser(userReq);
    }

    // 更新用戶
    public void updateUser(Long id, UserReq userReq) {
        UserRes res = userRepository.getUserById(Math.toIntExact(id));
        res.setId(userReq.getUserId());
        res.setPassword(userReq.getPassword());
        res.setNickName(userReq.getNickName());
        res.setEmail(userReq.getEmail());
        res.setPhoneNumber(userReq.getPhoneNumber());
res.setAddress(userReq.getAddress());
res.setUpdatedAt(LocalDateTime.now());

        userRepository.update(res);
    }

    // 刪除用戶
    public void deleteUser(Long id) {
        userRepository.deleteUser(Math.toIntExact(id));
    }



    public int getUserCountByRoleId(int roleId) {
        return userRepository.countByRoleId(roleId);
    }
}