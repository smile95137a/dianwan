package com.one.frontend.service;

import com.one.frontend.model.Role;
import com.one.frontend.model.User;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.UserReq;
import com.one.frontend.response.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUser(){
        return userRepository.getAllUser();
    }


    public UserRes getUserById(Integer userId) {
        return userRepository.getUserById(userId);
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

    public UserRes registerUser(UserReq userDto) throws Exception {
        User check = userRepository.getUserByUserName(userDto.getUsername());
        if(check != null){
            throw new Exception("帳號已存在");
        }

        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(encryptedPassword);
            user.setEmail(userDto.getEmail());
            user.setAddress(userDto.getAddress());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setCreatedAt(LocalDateTime.now());
            user.setRoleId(2L); //註冊即是正式會員
            user.setBalance(BigDecimal.valueOf(0));
            user.setBonus(BigDecimal.valueOf(0));
            userRepository.createUser(user);
            UserRes userRes = new UserRes();
            userRes.setUsername(user.getUsername());
            userRes.setEmail(user.getEmail());
            userRes.setPhoneNumber(user.getPhoneNumber());
            userRes.setAddress(user.getAddress());
            return userRes;
    }

    public UserRes updateUser(UserReq userReq , Integer id) {
            String encryptedPassword = passwordEncoder.encode(userReq.getPassword());
            User user = userRepository.getUserBId(Math.toIntExact(id));
            user.setPassword(encryptedPassword);
            user.setNickname(userReq.getNickname());
            user.setEmail(userReq.getEmail());
            user.setAddress(userReq.getAddress());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.update(user);
            return userRepository.getUserById(id);
    }
}