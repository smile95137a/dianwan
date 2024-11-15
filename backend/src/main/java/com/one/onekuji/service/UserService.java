package com.one.onekuji.service;

import com.one.onekuji.model.User;
import com.one.onekuji.repository.RoleRepository;
import com.one.onekuji.repository.UserRepository;
import com.one.onekuji.request.SliverUpdate;
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

    public void updateSliver(SliverUpdate sliverUpdate) {
        if (sliverUpdate != null && sliverUpdate.getUserId() != null && !sliverUpdate.getUserId().isEmpty()) {
            // 调用批量更新银币的方法
            userRepository.updateSliverCoinBatch(
                    sliverUpdate.getUserId(),
                    sliverUpdate.getSliverCoin(),
                    sliverUpdate.getBonus()
            );
        } else {
            // 如果用户ID列表为空，抛出异常或处理其他逻辑
            throw new IllegalArgumentException("User ID list cannot be null or empty");
        }
    }

}
