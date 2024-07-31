package com.one.frontend.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
