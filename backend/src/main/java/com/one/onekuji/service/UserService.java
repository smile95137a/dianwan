package com.one.onekuji.service;

import com.one.onekuji.model.User;
import com.one.onekuji.repository.RoleRepository;
import com.one.onekuji.repository.UserRepository;
import com.one.onekuji.request.UserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUser(){
        return userRepository.getAllUser();
    }
    public String createUser(UserReq userReq) throws Exception {
        User check = userRepository.getUserByUserName(userReq.getUsername());
        if(check != null){
            throw new Exception("用戶已存在");
        }
        String encryptedPassword = passwordEncoder.encode(userReq.getPassword());
        try {
            User user = new User();
            user.setUsername(userReq.getUsername());
            user.setPassword(encryptedPassword);
            user.setNickname(userReq.getNickname());
            user.setEmail(userReq.getEmail());
            user.setAddress(userReq.getAddress());
            user.setCreatedAt(LocalDateTime.now());
            user.setRoleId(2L);
            user.setBalance(BigDecimal.valueOf(0));
            user.setBonus(BigDecimal.valueOf(0));
            userRepository.createUser(user);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public User getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    public String updateUser(UserReq userReq) {
        try {
            String encryptedPassword = passwordEncoder.encode(userReq.getPassword());
            User user = userRepository.getUserById(userReq.getId());
            user.setPassword(encryptedPassword);
            user.setNickname(userReq.getNickname());
            user.setEmail(userReq.getEmail());
            user.setAddress(userReq.getAddress());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.update(user);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public String deleteUser(Integer userId) {
        try{
            userRepository.deleteUser(userId);
            return "1";
        }catch (Exception e){
            return "0";
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

        // 创建并返回 CustomUserDetails
        return new CustomUserDetails(user);
    }



    public int getUserCountByRoleId(int roleId) {
        return userRepository.countByRoleId(roleId);
    }
}