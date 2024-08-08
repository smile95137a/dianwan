package com.one.frontend.controller;

import com.one.frontend.dto.UserDto;
import com.one.frontend.model.User;
import com.one.frontend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "使用者管理", description = "與使用者相關的操作")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "獲取所有使用者", description = "檢索所有使用者的列表")
    @GetMapping("/query")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "通過 ID 獲取使用者", description = "根據其 ID 獲取使用者")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "使用者檢索成功"),
            @ApiResponse(responseCode = "404", description = "使用者未找到")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "使用者的 ID", example = "1") @PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) throws Exception {

        String isSuccess = userService.registerUser(userDto);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("創建成功", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("創建失敗", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "更新現有使用者", description = "根據 ID 更新現有的使用者")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "使用者更新成功"),
            @ApiResponse(responseCode = "404", description = "使用者未找到")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(
            @Parameter(description = "使用者的 ID", example = "1") @PathVariable Integer userId,
            @Parameter(description = "要更新的使用者詳細信息") @RequestBody UserDto user) {
        User existingUser = userService.getUserById(userId);
        if (existingUser != null) {
            String isSuccess = userService.updateUser(user);
            if ("1".equals(isSuccess)) {
                return new ResponseEntity<>("更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("更新失敗", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("使用者未找到", HttpStatus.NOT_FOUND);
        }
    }

}
