package com.one.onekuji.controller;

import com.one.model.User;
import com.one.onekuji.request.UserReq;
import com.one.onekuji.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
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

    @Operation(summary = "創建新的使用者", description = "創建一個新的使用者")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "使用者創建成功"),
            @ApiResponse(responseCode = "400", description = "無效的輸入")
    })
    @PostMapping("/add")
    public ResponseEntity<String> createUser(
            @Parameter(description = "要創建的使用者詳細信息") @RequestBody UserReq user) throws Exception {
        String isSuccess = userService.createUser(user);
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
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(
            @Parameter(description = "使用者的 ID", example = "1") @PathVariable Integer userId,
            @Parameter(description = "要更新的使用者詳細信息") @RequestBody UserReq user) {
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

    @Operation(summary = "刪除使用者", description = "根據 ID 刪除使用者")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "使用者刪除成功"),
            @ApiResponse(responseCode = "404", description = "使用者未找到")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "使用者的 ID", example = "1") @PathVariable Integer userId) {
        String isSuccess = userService.deleteUser(userId);
        if ("1".equals(isSuccess)) {
            return new ResponseEntity<>("刪除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("刪除失敗", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getUserCount() {
        int adminUserCount = userService.getUserCountByRoleId(1);
//        int regularUserCount = userService.getUserCountByRoleId(2);
//        int trialUserCount = userService.getUserCountByRoleId(3);

        Map<String, Integer> response = new HashMap<>();
        response.put("adminUserCount", adminUserCount);
//        response.put("regularUserCount", regularUserCount);
//        response.put("trialUserCount", trialUserCount);

        return ResponseEntity.ok(response);
    }

}
