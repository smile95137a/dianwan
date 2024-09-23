package com.one.onekuji.service;

import com.one.onekuji.model.User;
import com.one.onekuji.repository.RoleRepository;
import com.one.onekuji.repository.UserRepository;
import com.one.onekuji.request.UserReq;
import com.one.onekuji.response.UserRes;
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

    public User createUser(UserReq userReq) {
        User user = new User();
        user.setUsername(userReq.getUsername());
        user.setPassword(userReq.getPassword());
        user.setNickname(userReq.getNickName());
        user.setEmail(userReq.getEmail());
        user.setPhoneNumber(userReq.getPhoneNumber());
        user.setAddress(userReq.getAddress());
        user.setRoleId(userReq.getRoleId());
        user.setBalance(userReq.getBalance());
        user.setCreatedAt(LocalDateTime.now());

        userRepository.insert(user);

        return user;
    }

    public User updateUser(Long userId, UserReq userReq) {
        User user = userRepository.findById2(userId);
        if (user != null) {
            user.setUsername(userReq.getUsername());
            user.setPassword(userReq.getPassword());
            user.setNickname(userReq.getNickName());
            user.setEmail(userReq.getEmail());
            user.setPhoneNumber(userReq.getPhoneNumber());
            user.setAddress(userReq.getAddress());
            user.setRoleId(userReq.getRoleId());
            user.setBalance(userReq.getBalance());
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.update(user);
        }

        return user;
    }

    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }
}
