package com.one.onekuji.service;

import com.one.onekuji.model.User;
import com.one.onekuji.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

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
}