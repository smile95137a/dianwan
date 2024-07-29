package com.one.onekuji.service;

import com.one.model.Role;
import com.one.model.User;
import com.one.onekuji.request.UserReq;
import com.one.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUser(){
        return userRepository.getAllUser();
    }

    public String createUser(UserReq userReq) throws Exception {
        User check = userRepository.getUserByUserName(userReq.getUsername());
        if(check != null){
            throw new Exception("用戶已存在");
        }
        try {
            User user = new User();
            user.setUsername(userReq.getUsername());
            user.setPassword(userReq.getPassword());
            user.setNickname(userReq.getNickname());
            user.setEmail(userReq.getEmail());
            user.setAddress(userReq.getAddress());
            user.setUserType(User.UserType.USER);
            user.setCreatedAt(LocalDateTime.now());
            user.setRoleId(2);
            userRepository.createUser(user);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    public User getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    public String updateUser(UserReq userReq) {
        try {
            User user = userRepository.getUserById(userReq.getId());
            user.setPassword(userReq.getPassword());
            user.setNickname(userReq.getNickname());
            user.setEmail(userReq.getEmail());
            user.setAddress(userReq.getAddress());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.update(user);
            return "1";
        } catch (Exception e) {
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

        Set<Role> roles = user.getRoles();
        Set<GrantedAuthority> authorities = (roles != null ? roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet()) : Collections.emptySet());
        authorities.forEach(System.out::println);
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                authorities
        );
    }

    public int getUserCountByRoleId(int roleId) {
        return userRepository.countByRoleId(roleId);
    }
}