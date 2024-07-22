package com.one.onekuji.controller;

import com.one.onekuji.model.User;
import com.one.onekuji.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User Management", description = "User management API")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> user = userService.getAllUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Create a new user", description = "Create a new user")
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String isSuccess = userService.createUser(user);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Update user", description = "Update an existing user by their ID")
    @PutMapping("/user/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        String isSuccess = userService.updateUser(user);

        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("更新成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("更新失敗", HttpStatus.CREATED);
        }
    }

    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        String isSuccess = userService.deleteUser(userId);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("刪除成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.CREATED);
        }
    }
}
