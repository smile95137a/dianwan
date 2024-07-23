package com.one.onekuji.service;

import com.one.onekuji.model.User;
import com.one.onekuji.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public String createUser(User user) {
        try {
            userRepository.createUser(user);
            return "1";
        } catch (Exception e) {
            return "0";
        }
    }

    public User getUserById(Integer userId) {
        return userRepository.getUserById(userId);
    }

    public String updateUser(User user) {
        try {
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
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail,
                user.getPassword(),
                authorities
        );
    }
}